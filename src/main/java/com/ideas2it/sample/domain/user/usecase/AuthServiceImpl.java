package com.ideas2it.sample.domain.user.usecase;


import com.ideas2it.sample.infrastructure.config.JwtTokenUtil;
import com.ideas2it.sample.domain.user.dto.AuthRequestDto;
import com.ideas2it.sample.domain.user.dto.AuthResponseDto;
import com.ideas2it.sample.domain.user.dto.RegisterRequestDto;
import com.ideas2it.sample.domain.user.dto.RegisterResponseDto;
import com.ideas2it.sample.infrastructure.exception.AlreadyExistsException;
import com.ideas2it.sample.infrastructure.exception.ResourceNotFoundException;
import com.ideas2it.sample.infrastructure.exception.BadCredentialsException;
import com.ideas2it.sample.domain.user.mapper.UserMapper;
import com.ideas2it.sample.domain.role.model.Role;
import com.ideas2it.sample.domain.user.model.User;
import com.ideas2it.sample.adaptor.out.RoleRepository;
import com.ideas2it.sample.adaptor.out.UserRepository;
import com.ideas2it.sample.domain.user.port.in.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private static final Logger LOGGER = LogManager.getLogger(AuthServiceImpl.class);

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
        LOGGER.info("New user registered with Email : " + request.getEmail());
        return userMapper.toDto(user);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        User user = checkUserAvailable(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            LOGGER.warn("Invalid credentials found");
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtTokenUtil.generateToken(user);
        LOGGER.info("Login successful with Email : " + request.getEmail());
        return userMapper.toLoginResponse(user, token);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findUserIdByEmail(email);
        return user.getId();
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            LOGGER.warn("User already exists with same email.");
            throw new AlreadyExistsException("A user with this email already exists.");
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
