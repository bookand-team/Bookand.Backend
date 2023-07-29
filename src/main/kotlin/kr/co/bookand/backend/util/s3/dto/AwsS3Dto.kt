package kr.co.bookand.backend.util.s3.dto

import org.springframework.web.multipart.MultipartFile

data class UploadFileRequest(
    val type: String,
    val files: List<MultipartFile>
)

data class UpdateFileRequest(
    val type: String,
    val toDeleteUrls: List<String>,
    val newFiles: List<MultipartFile>
)

data class FileDto(
    val fileName: String,
    val fileUrl: String
)

data class FileListDto(
    val files: List<FileDto>
)