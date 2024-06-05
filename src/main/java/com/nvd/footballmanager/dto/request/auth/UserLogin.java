package com.nvd.footballmanager.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogin {
    @Email(message = "Email invalid")
    private String email;
    @Size(min = 6, max = 18, message = "Size password error")
    private String password;
}
