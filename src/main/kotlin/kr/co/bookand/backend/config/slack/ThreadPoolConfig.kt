package kr.co.bookand.backend.config.slack

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor


@EnableAsync
@Configuration
class ThreadPoolConfig {
    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.maxPoolSize = 3
        threadPoolTaskExecutor.corePoolSize = 3
        threadPoolTaskExecutor.initialize()
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-")
        return threadPoolTaskExecutor
    }
}