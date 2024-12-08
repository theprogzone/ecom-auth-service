package com.ecommerce.ecom_auth_service.service;

import com.ecommerce.ecom_auth_service.dto.LoginDTO;
import com.ecommerce.ecom_auth_service.dto.RegisterDTO;

public interface UserService {
    void registerUser(RegisterDTO registerDTO);

    String login(LoginDTO loginDTO);
}
