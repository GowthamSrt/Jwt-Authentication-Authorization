package com.ideas2it.sample.domain.user.mapper;

import com.ideas2it.sample.domain.user.dto.AuthResponseDto;
import com.ideas2it.sample.domain.user.dto.RegisterRequestDto;
import com.ideas2it.sample.domain.user.dto.RegisterResponseDto;
import com.ideas2it.sample.domain.role.model.Role;
import com.ideas2it.sample.domain.user.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User toEntity(RegisterRequestDto requestDto, String encodedPassword, Role role) {
        return User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .isAdmin(requestDto.getIsAdmin())
                .roles(Set.of(role))
                .build();
    }

    public RegisterResponseDto toDto(User user) {
        return RegisterResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .message("Registered successfully!!!")
                .build();
    }

    public AuthResponseDto toLoginResponse(User user, String token) {
        return AuthResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .token(token)
                .build();
    }
}
