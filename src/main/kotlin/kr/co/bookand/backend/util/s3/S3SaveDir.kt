package kr.co.bookand.backend.util.s3

enum class S3SaveDir(
    var path: String
) {
    ACCOUNT_PROFILE("/account/profileImage"),
    ARTICLE("/article/mainImage"),
    REPORT_LOG("/report/log");

}