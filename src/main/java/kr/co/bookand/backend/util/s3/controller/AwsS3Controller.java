package kr.co.bookand.backend.util.s3.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.bookand.backend.common.domain.dto.ApiResponse;
import kr.co.bookand.backend.util.s3.dto.AwsS3Dto.*;
import kr.co.bookand.backend.util.s3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static kr.co.bookand.backend.common.domain.dto.ApiResponse.success;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
@Api(tags = "S3 이미지 API")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @ApiOperation(value = "이미지 업로드")
    @PostMapping()
    public ApiResponse<FileListDto> uploadFiles(@ModelAttribute UploadFileRequest uploadFileRequest) {
        return success(awsS3Service.uploadFiles(uploadFileRequest));
    }
    
    @ApiOperation(value = "이미지 업데이트")
    @PostMapping("/change")
    public ApiResponse<FileListDto> updateFiles(@ModelAttribute UpdateFileRequest updateFileRequest) {
        return success(awsS3Service.updateFiles(updateFileRequest));
    }

    @ApiOperation(value = "이미지 삭제")
    @DeleteMapping("/{type}")
    public ApiResponse<?> deleteFiles(@PathVariable String type, @RequestParam String fileUrl) {
        awsS3Service.deleteFiles(type, fileUrl);
        return success("DELETE SUCCESS");
    }
}
