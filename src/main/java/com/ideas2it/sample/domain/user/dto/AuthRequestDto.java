package com.ideas2it.sample.domain.user.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;
}
