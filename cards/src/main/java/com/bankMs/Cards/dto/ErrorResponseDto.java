package com.example.Cards.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    public String apiPath;

    public HttpStatus errorCode;

    private  String errorMessage;

    private LocalDateTime errorTime;
}
