package com.ideas2it.sample.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDto {
    private String username;
    private String email;
    private String password;
    private Boolean isAdmin;
}
