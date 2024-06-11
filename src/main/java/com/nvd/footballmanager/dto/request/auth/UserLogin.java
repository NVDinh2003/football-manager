package com.nvd.footballmanager.dto.request.auth;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogin {
    @Size(min = 3, max = 30, message = "Size username error")
    private String username;
    @Size(min = 6, max = 18, message = "Size password error")
    private String password;
}
