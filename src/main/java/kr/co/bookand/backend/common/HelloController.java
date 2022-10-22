package kr.co.bookand.backend.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("")
    public String hello() {
        return "BOOKAND REST API Server is running!";
    }
}
