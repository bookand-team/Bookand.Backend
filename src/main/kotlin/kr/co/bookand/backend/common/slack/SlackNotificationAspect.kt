package kr.co.bookand.backend.common.slack


import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackField
import net.gpedro.integrations.slack.SlackMessage
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest


@Aspect
@Component
class SlackNotificationAspect(
    private val slackApi: SlackApi,
    private val threadPoolTaskExecutor: ThreadPoolTaskExecutor
) {
    @Around("@annotation(SlackNotification) && args(request, e)")
    fun slackNotification(
        proceedingJoinPoint: ProceedingJoinPoint,
        request: HttpServletRequest,
        e: Exception
    ) {
        proceedingJoinPoint.proceed()
        val requestInfo = RequestInfo(
            requestUrl = request.requestURL.toString(),
            method = request.method,
            remoteAddr = request.remoteAddr
        )
        threadPoolTaskExecutor.execute { sendSlackMessage(requestInfo, e) }
    }

    fun sendSlackMessage(requestInfo: RequestInfo, e: Exception) {
        val slackMessage = constructSlackMessage(requestInfo, e)
        slackApi.call(slackMessage)
    }

    private fun constructSlackMessage(requestInfo: RequestInfo, e: Exception): SlackMessage {
        val slackAttachment = constructSlackAttachment(requestInfo, e)
        val slackMessage = SlackMessage()
        slackMessage.setAttachments(listOf(slackAttachment))
        slackMessage.setText("!!에러 발생!!")
        slackMessage.setUsername("BookAnd 에러 알림")
        return slackMessage
    }

    private fun constructSlackAttachment(requestInfo: RequestInfo, e: Exception): SlackAttachment {
        val slackAttachment = SlackAttachment()
        slackAttachment.setFallback("Error")
        slackAttachment.setColor("danger")
        slackAttachment.setFields(listOf(
            constructSlackField("Exception class", e.javaClass.canonicalName),
            constructSlackField("예외 메시지", e.message ?: ""),
            constructSlackField("Request URL", requestInfo.requestUrl),
            constructSlackField("Request Method", requestInfo.method),
            constructSlackField("요청 시간", currentTime()),
            constructSlackField("Request IP", requestInfo.remoteAddr)
        ))
        return slackAttachment
    }

    private fun constructSlackField(title: String, value: String): SlackField {
        val slackField = SlackField()
        slackField.setTitle(title)
        slackField.setValue(value)
        return slackField
    }

    private fun currentTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    }
}