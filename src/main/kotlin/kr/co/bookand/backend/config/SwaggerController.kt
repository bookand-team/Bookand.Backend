package kr.co.bookand.backend.config

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import springfox.documentation.annotations.ApiIgnore

@Controller
@ApiIgnore
class SwaggerController {
    @GetMapping("/docs")
    fun redirect(): String {
        return "redirect:/swagger-ui/index.html"
    }
}