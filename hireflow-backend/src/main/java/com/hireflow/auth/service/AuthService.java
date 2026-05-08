package com.hireflow.auth.service;

import com.hireflow.auth.dto.request.LoginRequest;
import com.hireflow.auth.dto.request.RegisterRequest;
import com.hireflow.auth.dto.response.LoginResponse;
import com.hireflow.auth.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
