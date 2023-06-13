package kr.co.bookand.backend.config

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import springfox.documentation.annotations.ApiIgnore

@Controller
@ApiIgnore
class KotlinSwaggerController {
    @GetMapping("/v3/docs")
    fun redirect(): String {
        return "redirect:/swagger-ui/index.html"
    }
}