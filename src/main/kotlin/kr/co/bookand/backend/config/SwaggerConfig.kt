package kr.co.bookand.backend.config

import com.fasterxml.classmate.TypeResolver
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.AlternateTypeRules
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig(private val typeResolver: TypeResolver) {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .host("dev.bookand.co.kr")
            .consumes(getConsumeContentTypes())
            .produces(getProduceContentTypes())
            .apiInfo(getApiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/**"))
            .build()
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(securityScheme()))
            .alternateTypeRules(
                AlternateTypeRules.newRule(
                    typeResolver.resolve(Pageable::class.java),
                    typeResolver.resolve(MyPageable::class.java)
                )
            )
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .operationSelector { true }
            .build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScopes = arrayOf(AuthorizationScope("global", "accessEverything"))
        return listOf(SecurityReference("Authorization", authorizationScopes))
    }

    private fun securityScheme(): ApiKey {
        return ApiKey("Authorization", "Authorization", "header")
    }

    private fun getConsumeContentTypes(): Set<String> {
        val consumes: MutableSet<String> = HashSet()
        consumes.add("application/json;charset=UTF-8")
        consumes.add("application/x-www-form-urlencoded")
        return consumes
    }

    private fun getProduceContentTypes(): Set<String> {
        val produces: MutableSet<String> = HashSet()
        produces.add("application/json;charset=UTF-8")
        return produces
    }

    private fun getApiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Book& REST API 문서")
            .description("각 기능에 대한 문서 제공")
            .version("1.0")
            .build()
    }

    @ApiModel
    data class MyPageable(
        @ApiModelProperty(value = "페이지 번호(0..N)", example = "0")
        val page: Int? = null,

        @ApiModelProperty(value = "페이지당 데이터 수", example = "10")
        val size: Int? = null
    )
}