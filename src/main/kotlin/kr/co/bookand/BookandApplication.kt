package kr.co.bookand

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate


@EnableScheduling
@SpringBootApplication
class BookandApplication

fun main(args: Array<String>) {
    runApplication<BookandApplication>(*args)
}

