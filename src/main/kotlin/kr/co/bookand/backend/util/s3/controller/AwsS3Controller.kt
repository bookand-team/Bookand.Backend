package kr.co.bookand.backend.util.s3.controller

import kr.co.bookand.backend.util.s3.dto.FileListDto
import kr.co.bookand.backend.util.s3.dto.UpdateFileRequest
import kr.co.bookand.backend.util.s3.dto.UploadFileRequest
import kr.co.bookand.backend.util.s3.service.AwsS3Service
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
class AwsS3Controller(
    val awsS3Service: AwsS3Service
) {
    @PostMapping()
    fun uploadFiles(@ModelAttribute uploadFileRequest: UploadFileRequest): ResponseEntity<FileListDto> {
        return ResponseEntity.ok(awsS3Service.uploadFiles(uploadFileRequest))
    }

    @PostMapping("/change")
    fun updateFiles(@ModelAttribute updateFileRequest: UpdateFileRequest): ResponseEntity<FileListDto> {
        return ResponseEntity.ok(awsS3Service.updateFiles(updateFileRequest))
    }

    @DeleteMapping("/{type}")
    fun deleteFiles(@PathVariable type: String, @RequestParam fileUrl: String): ResponseEntity<String> {
        awsS3Service.deleteFiles(type, fileUrl)
        return ResponseEntity.ok("DELETE SUCCESS")
    }
}