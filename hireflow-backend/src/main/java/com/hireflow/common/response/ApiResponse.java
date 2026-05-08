package com.hireflow.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    private int statusCode;

    private String message;

    private T data;

    private List<String> errors;

    private String path;

    private LocalDateTime timestamp;
}