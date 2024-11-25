package com.ideas2it.sample.domain.user.port.in;

import com.ideas2it.sample.domain.user.dto.AuthRequestDto;
import com.ideas2it.sample.domain.user.dto.AuthResponseDto;
import com.ideas2it.sample.domain.user.dto.RegisterRequestDto;
import com.ideas2it.sample.domain.user.dto.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto request);
    AuthResponseDto login(AuthRequestDto request);
    Long getUserIdByEmail(String email);
}
