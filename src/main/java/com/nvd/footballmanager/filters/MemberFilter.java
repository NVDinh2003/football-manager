package com.nvd.footballmanager.filters;

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
public class MemberFilter extends BaseFilter {
    private UUID teamId;
    private String position;
    private MemberRole role;
}
