package kr.co.bookand.backend.config.slack

import net.gpedro.integrations.slack.SlackApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlackLogAppenderConfig {

    @Value("\${logging.slack.webhook-uri}")
    lateinit var token: String

    @Bean
    fun slackApi(): SlackApi {
        return SlackApi(token)
    }
}