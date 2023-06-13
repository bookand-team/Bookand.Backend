package kr.co.bookand.backend.config;

import kr.co.bookand.backend.config.jwt.exception.JavaExceptionHandlerFilter;
import kr.co.bookand.backend.config.jwt.JavaJwtFilter;
import kr.co.bookand.backend.config.jwt.TokenFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JavaJwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenFactory tokenFactory;
    private final JavaExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    public void configure(HttpSecurity builder) {
        JavaJwtFilter customFilter = new JavaJwtFilter(tokenFactory);
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(exceptionHandlerFilter, JavaJwtFilter.class);
    }
}
