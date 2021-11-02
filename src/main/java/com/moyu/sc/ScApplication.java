package com.moyu.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableConfigurationProperties
public class ScApplication {

    @GetMapping("/curr_user")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/greeting")
    public String greeting() { return "Hello World"; }

    @GetMapping("/public/{everything}")
    public String publicResource() {
        return "Pub Res Here";
    }

    public static void main(String[] args) {
        SpringApplication.run(ScApplication.class, args);
    }

}
