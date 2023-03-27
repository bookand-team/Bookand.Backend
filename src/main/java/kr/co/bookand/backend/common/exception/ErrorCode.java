package kr.co.bookand.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    NOT_FOUND("COMMON-ERR-404", "PAGE NOT FOUND", "페이지를 찾을 수 없습니다.", 404),
    INPUT_VALID_ERROR("COMMON-ERR-400", "INPUT VALID ERROR", "입력값이 유효하지 않습니다.", 400),
    INTER_SERVER_ERROR("COMMON-ERR-500", "INTER SERVER ERROR", "서버 오류입니다.", 500),
    HTTP_CLIENT_ERROR("COMMON-ERR-401", "HTTP CLIENT ERROR", "HTTP 클라이언트 오류입니다.", 401),


    // Member
    NOT_ROLE_MEMBER("MEMBER-ERR-302", "NOT AUTHENTICATION ROLE", "인증 권한이 없습니다.", 403),
    NOT_FOUND_MEMBER("MEMBER-ERR-100", "NOT FOUND MEMBER", "멤버를 찾을 수 없습니다.", 404),
    ROLE_ACCESS_ERROR("MEMBER-ERR-101", "ROLE ACCESS ERROR", "권한이 없습니다.", 403),
    EMAIL_DUPLICATION("MEMBER-ERR-300", "EMAIL DUPLICATED", "중복된 이메일입니다.", 400),
    NICKNAME_DUPLICATION("MEMBER-ERR-301", "NICKNAME DUPLICATED", "중복된 닉네임입니다.", 400),
    APPLE_LOGIN_ERROR("MEMBER-ERR-201", "APPLE_LOGIN_ERROR", "애플 로그인 오류입니다.", 400),
    GOOGLE_LOGIN_ERROR("MEMBER-ERROR-200", "GOOGLE_LOGIN_ERROR", "구글 로그인 오류입니다.", 404),
    ALREADY_SIGNUP("MEMBER-ERR-202", "ALREADY_SIGNUP", "이미 가입된 회원입니다.", 400),
    INVALID_SIGN_TOKEN("MEMBER-ERR-203", "INVALID_SIGN_TOKEN", "유효하지 않은 토큰입니다.", 400),
    DELETED_ACCOUNT("MEMBER-ERR-204", "DELETED_ACCOUNT", "정지된 회원입니다.", 400),
    NOT_MATCH_MEMBER("MEMBER-ERR-205", "NOT_MATCH_MEMBER", "일치하는 회원이 없습니다.", 400),
    SUSPENDED_ACCOUNT("MEMBER-ERR-206", "SUSPENDED_ACCOUNT", "정지된 회원입니다.", 400),


    // JWT
    JWT_ERROR("JWT-ERR-100", "JWT EXCEPTION", "JWT 예외가 발생했습니다.", 401),
    JWT_ERROR_SIGNATURE("JWT-ERR-101", "SIGNATURE JWT ERROR", "잘못된 JWT 서명입니다.", 401),
    JWT_ERROR_EXPIRED("JWT-ERR-102", "EXPIRED JWT ERROR", "만료된 JWT입니다.", 401),
    JWT_ERROR_UNSUPPORTED("JWT-ERR-103", "UNSUPPORTED JWT ERROR", "지원되지 않는 JWT입니다.", 401),
    JWT_ERROR_ILLEGAL("JWT-ERR-104", "ILLEGAL ARGUMENT JWT ERROR", "잘못된 인수로 인한 JWT 오류입니다.", 401),
    NOT_FOUND_REFRESH_TOKEN("REFRESH_TOKEN-ERR-100", "NOT FOUND REFRESH_TOKEN", "리프레시 토큰을 찾을 수 없습니다.", 404),
    NOT_MATCH_REFRESH_TOKEN("REFRESH_TOKEN-ERR-101", "NOT MATCH REFRESH_TOKEN", "리프레시 토큰이 일치하지 않습니다.", 400),


    // Policy
    NOT_FOUND_POLICY("POLICY-ERR-100", "NOT FOUND POLICY", "정책을 찾을 수 없습니다.", 404),
    ALREADY_EXIST_POLICY("POLICY-ERR-101", "ALREADY EXIST POLICY", "이미 존재하는 정책입니다.", 400),


    // Bookstore
    NOT_FOUND_BOOKSTORE("BOOKSTORE-ERR-100", "NOT FOUND BOOKSTORE", "서점을 찾을 수 없습니다.", 404),
    DUPLICATE_BOOKSTORE_NAME("BOOKSTORE-ERR-101", "DUPLICATE BOOKSTORE NAME", "중복된 서점 이름입니다.", 400),
    NOT_FOUND_BOOKSTORE_IMAGE("BOOKSTORE-ERR-102", "NOT FOUND BOOKSTORE IMAGE", "서점 이미지를 찾을 수 없습니다.", 404),
    NOT_FOUND_BOOKSTORE_REPORT("BOOKSTORE-ERR-103", "NOT FOUND BOOKSTORE REPORT", "서점 제보를 찾을 수 없습니다.", 404),
    NOT_FOUND_ARTICLE_BOOKSTORE("ARTICLE_BOOKSTORE-ERR-100", "NOT FOUND ARTICLE BOOKSTORE", "서점 아티클을 찾을 수 없습니다.", 404),
    TOO_MANY_BOOKSTORE_THEME("BOOKSTORE-ERR-104", "TOO MANY BOOKSTORE THEME", "서점 테마 선택은 최대 3개까지 가능합니다.", 400),


    // Bookmark
    NOT_FOUND_BOOKMARK("BOOKMARK-ERR-100", "NOT FOUND BOOKMARK", "북마크를 찾을 수 없습니다.", 404),
    NOT_FOUND_INIT_BOOKMARK("BOOKMARK-ERR-101", "NOT FOUND INIT BOOKMARK", "모아보기 폴더가 생성되지 않았습니다.", 404),
    NOT_MATCH_BOOKMARK_TYPE("BOOKMARK-ERR-102", "NOT MATCH BOOKMARK TYPE", "북마크 타입이 일치하지 않습니다.", 400),
    NOT_CHANGE_INIT_BOOKMARK("BOOKMARK-ERR-103", "NOT CHANGE INIT BOOKMARK", "모아보기 폴더는 이름 변경이 불가능합니다.", 400),
    ALREADY_EXIST_BOOKMARK("BOOKMARK-ERR-104", "ALREADY EXIST BOOKMARK", "이미 존재하는 북마크입니다.", 400),

    // Article
    NOT_FOUND_ARTICLE("ARTICLE-ERR-100", "NOT FOUND ARTICLE", "아티클을 찾을 수 없습니다.", 404),
    DUPLICATE_ARTICLE("ARTICLE-ERR-101", "DUPLICATE ARTICLE", "중복된 아티클 제목입니다.", 400),

    NOT_FOUND_BOOKMARK_CONTENT("BOOKMARK_CONTENT-ERR-100", "NOT FOUND BOOKMARK_CONTENT", "북마크 내용을 찾을 수 없습니다.", 404),


    // AWS S3
    AWS_S3_UPLOAD_FAIL("AWS-S3-ERROR-100","AWS S3 upload fail.", "이미지 업로드를 실패하였습니다.", 400),
    AWS_S3_DELETE_FAIL( "AWS-S3-ERROR-101","AWS S3 delete fail.", "이미지 삭제를 실패하였습니다.", 400),
    AWS_S3_FILE_SIZE_EXCEEDED("AWS-S3-ERROR-102", "exceeded file size.", "이미지 최대 사이즈를 초과하였습니다.", 400),


    // Feedback
    NOT_FOUND_FEEDBACK("FEEDBACK-ERR-100", "NOT FOUND FEEDBACK", "피드백을 찾을 수 없습니다.", 404),


    // Notification
    NOT_FOUND_NOTIFICATION("NOTIFICATION-ERR-100", "NOT FOUND NOTIFICATION", "공지사항을 찾을 수 없습니다.", 404),

    // Map
    JSON_PROCESSING_ERROR("MAP-ERR-100", "JSON PROCESSING ERROR", "JSON 파싱 오류입니다.", 400);

    private String errorCode;
    private String errorLog;
    private String errorMessage;
    private int httpStatus;
}