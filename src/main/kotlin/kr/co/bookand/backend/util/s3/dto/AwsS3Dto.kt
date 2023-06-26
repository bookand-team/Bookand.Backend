package kr.co.bookand.backend.util.s3.dto

import org.springframework.web.multipart.MultipartFile

data class UploadFileRequest(
    val type: String,
    val files: List<MultipartFile>
)

data class UpdateFileRequest(
    val type: String,
    val newFiles: List<MultipartFile>,
    val toDeleteUrls: List<String>
)

data class FileDto(
    val url: String,
    val fileName: String
)

data class FileListDto(
    val files: List<FileDto>
)