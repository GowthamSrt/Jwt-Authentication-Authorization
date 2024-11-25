package com.ideas2it.sample.adaptor.in;

import com.ideas2it.sample.domain.user.dto.AuthRequestDto;
import com.ideas2it.sample.domain.user.dto.AuthResponseDto;
import com.ideas2it.sample.domain.user.dto.RegisterRequestDto;
import com.ideas2it.sample.domain.user.dto.RegisterResponseDto;
import com.ideas2it.sample.domain.user.port.in.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
