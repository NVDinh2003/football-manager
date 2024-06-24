package com.nvd.footballmanager.dto;

import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.model.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private UUID id;
    private String nickname;
    private String position;
    private int shirtNumber;
    private MemberRole role;
    private double fee;

    private UserDTO user;

    private TeamDTO team;
}
