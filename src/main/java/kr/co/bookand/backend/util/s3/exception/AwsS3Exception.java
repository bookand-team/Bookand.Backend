package kr.co.bookand.backend.util.s3.exception;

import kr.co.bookand.backend.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AwsS3Exception extends RuntimeException{
    ErrorCode awsS3ErrorCode;

    public AwsS3Exception(ErrorCode awsS3ErrorCode) {
        super(awsS3ErrorCode.getMessage());
        this.awsS3ErrorCode = awsS3ErrorCode;
    }
}
