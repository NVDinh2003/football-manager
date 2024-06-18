package com.nvd.footballmanager.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    //    private UserDTO user;
    private String accessToken;
    private String refreshToken;
}