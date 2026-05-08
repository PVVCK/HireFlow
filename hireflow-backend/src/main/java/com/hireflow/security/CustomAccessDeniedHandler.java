package com.hireflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hireflow.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws java.io.IOException {

        log.warn(
                "Access denied | URI: {} | Method: {} | Reason: {}",
                request.getRequestURI(),
                request.getMethod(),
                accessDeniedException.getMessage()
        );

        ApiResponse<Object> apiResponse =
                ApiResponse.builder()
                        .success(false)
                        .statusCode(403)
                        .message("Access denied : Insufficient Role Privileges")
                        .data(null)
                        .errors(null)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.getWriter().write(
                mapper.writeValueAsString(apiResponse)
        );

        response.getWriter().flush();
    }
}