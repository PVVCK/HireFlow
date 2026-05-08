package com.hireflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hireflow.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws java.io.IOException {

        log.warn(
                "Unauthorized access attempt | URI: {} | Method: {} | Reason: {}",
                request.getRequestURI(),
                request.getMethod(),
                authException.getMessage()
        );

        ApiResponse<Object> apiResponse =
                ApiResponse.builder()
                        .success(false)
                        .statusCode(401)
                        .message("Authentication required : Access Token is missing or invalid")
                        .data(null)
                        .errors(null)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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