package kr.co.bookand.backend.config;

import kr.co.bookand.backend.config.jwt.exception.ExceptionHandlerFilter;
import kr.co.bookand.backend.config.jwt.JwtFilter;
import kr.co.bookand.backend.config.jwt.TokenFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenFactory tokenFactory;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    public void configure(HttpSecurity builder) {
        JwtFilter customFilter = new JwtFilter(tokenFactory);
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(exceptionHandlerFilter, JwtFilter.class);
    }
}
