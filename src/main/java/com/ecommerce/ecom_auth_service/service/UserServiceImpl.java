package com.ecommerce.ecom_auth_service.service;

import com.ecommerce.ecom_auth_service.dto.LoginDTO;
import com.ecommerce.ecom_auth_service.dto.RegisterDTO;
import com.ecommerce.ecom_auth_service.entity.User;
import com.ecommerce.ecom_auth_service.exception.ValidationException;
import com.ecommerce.ecom_auth_service.repository.UserRepository;
import com.ecommerce.ecom_auth_service.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @SneakyThrows
    @Override
    public void registerUser(RegisterDTO registerDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(registerDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new ValidationException("User already registered");
        }
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @SneakyThrows
    public String login(LoginDTO loginDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(loginDTO.getUsername());
        if (optionalUser.isEmpty()) {
            throw new ValidationException("User cannot find");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), optionalUser.get().getPassword())) {
            throw new ValidationException("Incorrect password");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", optionalUser.get().getEmail());
        claims.put("role", "ADMIN");
        return jwtUtil.generateJwtToken(optionalUser.get().getEmail(), claims);
    }

    @Override
    @SneakyThrows
    public void validateToken(String token) {
        log.info("Validate token");
        jwtUtil.validateToken(token);
        String userName = jwtUtil.extractUsername(token);
        Optional<User> optionalUser = userRepository.findByEmail(userName);
        if (optionalUser.isEmpty()) {
            throw new ValidationException("Invalid username");
        }
    }
}
