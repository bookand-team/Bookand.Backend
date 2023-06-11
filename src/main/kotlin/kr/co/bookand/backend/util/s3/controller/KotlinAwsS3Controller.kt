package kr.co.bookand.backend.util.s3.controller

import kr.co.bookand.backend.util.s3.dto.FileListDto
import kr.co.bookand.backend.util.s3.dto.UpdateFileRequest
import kr.co.bookand.backend.util.s3.dto.UploadFileRequest
import kr.co.bookand.backend.util.s3.service.KotlinAwsS3Service
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/files")
@RequiredArgsConstructor
class KotlinAwsS3Controller(
    val kotlinAwsS3Service: KotlinAwsS3Service
) {
    @PostMapping()
    fun uploadFiles(@ModelAttribute uploadFileRequest: UploadFileRequest): ResponseEntity<FileListDto> {
        return ResponseEntity.ok(kotlinAwsS3Service.uploadFiles(uploadFileRequest))
    }

    @PostMapping("/change")
    fun updateFiles(@ModelAttribute updateFileRequest: UpdateFileRequest): ResponseEntity<FileListDto> {
        return ResponseEntity.ok(kotlinAwsS3Service.updateFiles(updateFileRequest))
    }

    @DeleteMapping("/{type}")
    fun deleteFiles(@PathVariable type: String, @RequestParam fileUrl: String): ResponseEntity<String> {
        kotlinAwsS3Service.deleteFiles(type, fileUrl)
        return ResponseEntity.ok("DELETE SUCCESS")
    }
}