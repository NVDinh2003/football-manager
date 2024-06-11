package com.nvd.footballmanager.dto.response.auth;

import com.nvd.footballmanager.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private User user;
    private String token;
}