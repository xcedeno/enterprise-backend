package com.xavier.enterprise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    public record StatusResponse(
        String status,
        String message,
        String developer,
        String aiEngine,
        LocalDateTime timestamp
    ) {}

    @GetMapping
    public StatusResponse getStatus() {
        return new StatusResponse(
            "UP",
            "Microservicio Enterprise Spring Boot funcionando correctamente",
            "Xavier Cedeño Febres",
            "Google Antigravity",
            LocalDateTime.now()
        );
    }
}

