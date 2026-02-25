package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeUserDTO {

    private Long employeeId;
    private String fullName;

    public LikeUserDTO(Long employeeId, String fullName) {
        this.employeeId = employeeId;
        this.fullName = fullName;
    }
}