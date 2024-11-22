package com.ideas2it.sample.service;

import com.ideas2it.sample.dto.AuthRequestDto;
import com.ideas2it.sample.dto.AuthResponseDto;
import com.ideas2it.sample.dto.RegisterRequestDto;
import com.ideas2it.sample.dto.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto request);
    AuthResponseDto login(AuthRequestDto request);
    Long getUserIdByEmail(String email);
}
