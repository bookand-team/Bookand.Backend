package kr.co.bookand.backend.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JavaWebConfig implements WebMvcConfigurer {
    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
    public static final String ALLOWED_HEADER_NAMES = "Origin, X-Requested-With, Content-Type, Accept, Authorization";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://api.bookand.co.kr", "https://dev.bookand.co.kr" ,"http://localhost:3000", "https://bookand-admin-page.vercel.app/", "https://bookand-admin-page/")
                .allowedHeaders(ALLOWED_HEADER_NAMES)
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","));
    }

}
