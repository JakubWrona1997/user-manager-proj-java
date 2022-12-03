package com.usermanagerproj.dto.user.response;

import com.usermanagerproj.domain.role.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDto {
    private ERole name;
}
