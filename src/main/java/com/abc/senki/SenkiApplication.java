package com.abc.senki;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication()
@RequiredArgsConstructor
@EnableWebMvc

public class SenkiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenkiApplication.class, args);
    }

}
