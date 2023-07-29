package kr.co.bookand.backend.util.s3.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.exception.BookandException
import kr.co.bookand.backend.config.security.SecurityUtils.getCurrentAccountEmail
import kr.co.bookand.backend.util.s3.S3SaveDir
import kr.co.bookand.backend.util.s3.dto.FileDto
import kr.co.bookand.backend.util.s3.dto.FileListDto
import kr.co.bookand.backend.util.s3.dto.UpdateFileRequest
import kr.co.bookand.backend.util.s3.dto.UploadFileRequest
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import java.util.*

@Slf4j
@Component
@RequiredArgsConstructor
class AwsS3Service(
    private val amazonS3Client: AmazonS3Client
) {

    private val FILE_EXTENSION_SEPARATOR = "."

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String


    fun uploadFiles(uploadFileRequest: UploadFileRequest): FileListDto {
        val currentAccountEmail = getCurrentAccountEmail()
        val files = uploadFileRequest.files
        if (CollectionUtils.isEmpty(files)) {
            throw BookandException(ErrorCode.INPUT_VALID_ERROR)
        }
        val result = uploadFileRequest.files
            .map { file: MultipartFile -> uploadV2(file, uploadFileRequest.type, currentAccountEmail) }
            .toList()
        return FileListDto(result)
    }

    fun uploadV2(multipartFile: MultipartFile, type: String, owner: String): FileDto {
        validateFileExists(multipartFile)
        val fileName = buildFileName(Objects.requireNonNull(multipartFile.originalFilename), owner)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = multipartFile.contentType
        val savePath = getS3SaveDir(type)
        val bucketPath = bucket + savePath.path

        try {
            multipartFile.inputStream.use { inputStream ->
                amazonS3Client.putObject(
                    PutObjectRequest(bucketPath, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                )
            }
        } catch (e: RuntimeException) {
            throw BookandException(ErrorCode.AWS_S3_UPLOAD_FAIL)
        } catch (e: IOException) {
            throw BookandException(ErrorCode.AWS_S3_UPLOAD_FAIL)
        }

        val fileUrl = amazonS3Client.getUrl(bucketPath, fileName).toString()
        return FileDto(multipartFile.originalFilename, fileUrl)
    }

    private fun validateFileExists(multipartFile: MultipartFile) {
        if (multipartFile.isEmpty) {
            throw BookandException(ErrorCode.AWS_S3_UPLOAD_FAIL)
        }
    }

    private fun buildFileName(originalFileName: String, owner: String?): String {
        val fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val fileExtension = originalFileName.substring(fileExtensionIndex)
        return MessageFormat.format("{0}_{1}_{2}", owner, UUID.randomUUID(), fileExtension)
    }

    private fun getS3SaveDir(type: String): S3SaveDir {
        return when (type) {
            "profile" -> S3SaveDir.ACCOUNT_PROFILE
            "article" -> S3SaveDir.ARTICLE
            "reportLog" -> S3SaveDir.REPORT_LOG
            else -> S3SaveDir.ARTICLE
        }
    }

    fun updateFiles(updateFileRequest: UpdateFileRequest): FileListDto {
        val type = updateFileRequest.type
        val loginUser = getCurrentAccountEmail()
        val urls = updateFileRequest.toDeleteUrls
        if (CollectionUtils.isEmpty(urls)) {
            throw BookandException(ErrorCode.AWS_S3_UPLOAD_FAIL)
        }
        updateFileRequest.toDeleteUrls
            .forEach { file-> delete(type=type, url=file) }

        return FileListDto(updateFileRequest.newFiles
            .map<MultipartFile, FileDto> { file: MultipartFile -> uploadV2(file, type, loginUser) }
            .toList())
    }

    fun deleteFiles(type: String, url: String) {
        delete(type, url)
    }

    fun delete(type: String, url: String) {
        val filename = getFilename(url)
        val savePath = getS3SaveDir(type)
        val bucketPath = bucket + savePath.path
        try {
            amazonS3Client.deleteObject(DeleteObjectRequest(bucketPath, filename))
        } catch (e: Exception) {
            throw BookandException(ErrorCode.AWS_S3_DELETE_FAIL)
        }
    }

    private fun getFilename(url: String): String {
        val parsedUrl = url.split("/")
        val string = parsedUrl[parsedUrl.size - 1]
        return URLDecoder.decode(string, StandardCharsets.UTF_8)
    }

}