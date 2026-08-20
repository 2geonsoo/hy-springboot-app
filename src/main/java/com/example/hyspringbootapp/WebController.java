package com.example.hyspringbootapp;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    private final String appVersion;

    public WebController(@Value("${app.version:${APP_VERSION:dev}}") String appVersion) {
        this.appVersion = appVersion;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Hello, Spring Boot!",
                "version", appVersion);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/timecheck")
    public Map<String, String> timecheck() {
        return Map.of("time", OffsetDateTime.now().toString());
    }
}
