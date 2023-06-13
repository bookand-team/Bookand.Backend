package kr.co.bookand.backend.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
open class ETagHeaderFilter {
    @Bean
    open fun shallowEtagHeaderFilter(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val filterRegistrationBean = FilterRegistrationBean<ShallowEtagHeaderFilter>()
        filterRegistrationBean.filter = ShallowEtagHeaderFilter()
        filterRegistrationBean.addUrlPatterns("/api/v1/bookstores/address")
        filterRegistrationBean.setName("EtagAPIFilter")
        return filterRegistrationBean
    }
}