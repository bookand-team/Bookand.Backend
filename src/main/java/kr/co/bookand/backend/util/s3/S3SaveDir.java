package kr.co.bookand.backend.util.s3;

public enum S3SaveDir {
    ACCOUNT_PROFILE("/account/profileImage"),
    ARTICLE("/article/mainImage"),
    REPORT_LOG("/report/log"),

    ;

    public final String path;
    S3SaveDir(String path) {
        this.path = path;
    }
}
