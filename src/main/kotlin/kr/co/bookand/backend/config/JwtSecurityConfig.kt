package kr.co.bookand.backend.config

import kr.co.bookand.backend.config.jwt.JwtFilter
import kr.co.bookand.backend.config.jwt.JwtProvider
import kr.co.bookand.backend.config.jwt.exception.ExceptionHandlerFilter
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtSecurityConfig(
    private val jwtProvider: JwtProvider,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(builder: HttpSecurity) {
        val customFilter = JwtFilter(jwtProvider)
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
        builder.addFilterBefore(exceptionHandlerFilter, JwtFilter::class.java)
    }
}