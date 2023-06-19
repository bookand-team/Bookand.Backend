package kr.co.bookand.backend.config;

import kr.co.bookand.backend.config.jwt.*;
import kr.co.bookand.backend.config.jwt.exception.JavaExceptionHandlerFilter;
import kr.co.bookand.backend.config.jwt.exception.JavaJwtAccessDeniedHandler;
import kr.co.bookand.backend.config.jwt.exception.JavaJwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@RequiredArgsConstructor
public class JavaSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenFactory tokenFactory;
    private final JavaJwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JavaJwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JavaExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**", "/api/v1/members/nickname/**", "/api/v1/members/nickname/random/**").permitAll()
                .antMatchers("/api/v1/policys/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/docs/**", "/favicon.ico", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                        "/configuration/security", "/swagger-ui.html", "/swagger-ui/#", "/webjars/**", "/swagger/**", "/swagger-ui/**", "/", "/csrf", "/error").permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .apply(new JavaJwtSecurityConfig(tokenFactory, exceptionHandlerFilter));


    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://dev.bookand.co.kr", "http://localhost:3000", "http://api.bookand.co.kr", "https://bookand-admin-page.vercel.app/", "https://bookand-admin-page/"));
        configuration.setAllowedMethods(
                Arrays.asList(HttpMethod.POST.name(), HttpMethod.GET.name(),
                        HttpMethod.PUT.name(), HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name())
        );
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return source;
    }
}