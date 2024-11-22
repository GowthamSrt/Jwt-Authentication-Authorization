package com.ideas2it.sample.service.impl;


import com.ideas2it.sample.config.JwtTokenUtil;
import com.ideas2it.sample.dto.AuthRequestDto;
import com.ideas2it.sample.dto.AuthResponseDto;
import com.ideas2it.sample.dto.RegisterRequestDto;
import com.ideas2it.sample.dto.RegisterResponseDto;
import com.ideas2it.sample.exception.AlreadyExistsException;
import com.ideas2it.sample.exception.ResourceNotFoundException;
import com.ideas2it.sample.mapper.UserMapper;
import com.ideas2it.sample.model.Role;
import com.ideas2it.sample.model.User;
import com.ideas2it.sample.repository.RoleRepository;
import com.ideas2it.sample.repository.UserRepository;
import com.ideas2it.sample.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;

    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {
        validateEmail(request.getEmail());
        Role role = getRoleByAdminFlag(request.getIsAdmin());
        val user = userMapper.toEntity(request, passwordEncoder.encode(request.getPassword()),role);
        User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAdmin(request.getIsAdmin())
                .roles(Set.of(role))
                .build();
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        User user = checkUserAvailable(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtTokenUtil.generateToken(user);
        return userMapper.toLoginResponse(user, token);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findUserIdByEmail(email);
        return user.getId();
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistsException("A user with this email already exists");
        }
    }

    private User checkUserAvailable(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Role getRoleByAdminFlag(boolean isAdmin) {
        String roleName = isAdmin ? "ADMIN" : "USER";
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }
}
