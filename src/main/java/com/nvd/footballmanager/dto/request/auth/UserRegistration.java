package com.nvd.footballmanager.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserRegistration {
    @NotBlank(message = "Name not blank")
    @Size(min = 1, max = 30, message = "Size name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$", message = "Format error for name")
    private String name;
    @Email(message = "Email invalid")
    private String email;
    private String password;
}
