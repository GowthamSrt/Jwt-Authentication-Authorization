package com.ideas2it.sample.domain.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterResponseDto {
    private String username;
    private String email;
    private List<String> roles;
    private String message;
}
