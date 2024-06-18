package com.nvd.footballmanager.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogin {
    @NotBlank(message = "Username not blank")
    @Size(min = 3, max = 30, message = "Size username error")
    private String username;
    @NotBlank(message = "Password not blank")
    @Size(min = 8, max = 18, message = "Size password error")
    private String password;
}
