package com.nvd.footballmanager.dto.response.auth;

import com.nvd.footballmanager.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserDTO user;
    private String token;
}
