package com.nvd.footballmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO extends BaseDTO<UUID> {
    @NotBlank(message = "Name not blank")
    @Size(min = 1, max = 30, message = "size name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$", message = "format error for name")
    private String name;
    private String logo;
    private String address;
    private int rankPoints;
}
