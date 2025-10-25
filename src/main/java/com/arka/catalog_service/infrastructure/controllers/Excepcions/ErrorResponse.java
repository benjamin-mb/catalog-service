package com.arka.catalog_service.infrastructure.controllers.Excepcions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
}
