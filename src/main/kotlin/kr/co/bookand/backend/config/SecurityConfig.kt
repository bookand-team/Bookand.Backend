package kr.co.bookand.backend.config

import kr.co.bookand.backend.config.jwt.JwtProvider
import kr.co.bookand.backend.config.jwt.exception.*
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@EnableWebSecurity
@Configuration
open class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) : WebSecurityConfigurerAdapter() {

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    override fun configure(http: HttpSecurity) {
        http.httpBasic().disable()
            .csrf().disable()
            .cors()

            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/**", "/api/v1/members/nickname/**", "/api/v1/members/nickname/random/**").permitAll()
            .antMatchers("/api/v1/policys/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers(
                "/docs/**", "/favicon.ico", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                "/configuration/security", "/swagger-ui.html", "/swagger-ui/#", "/webjars/**", "/swagger/**", "/swagger-ui/**", "/", "/csrf", "/error").permitAll()
            .anyRequest().authenticated()

            .and()
            .apply(
                JwtSecurityConfig(jwtProvider, exceptionHandlerFilter)
            )
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.allowedOrigins = listOf(
            "https://dev.bookand.co.kr",
            "http://localhost:3000",
            "http://api.bookand.co.kr",
            "https://bookand-admin-page.vercel.app/",
            "https://bookand-admin-page/"
        )
        configuration.allowedMethods = listOf(
            HttpMethod.POST.name,
            HttpMethod.GET.name,
            HttpMethod.PUT.name,
            HttpMethod.DELETE.name,
            HttpMethod.OPTIONS.name
        )
        configuration.allowedHeaders = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = 0
        return source
    }
}