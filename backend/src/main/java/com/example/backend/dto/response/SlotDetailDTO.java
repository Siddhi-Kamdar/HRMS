package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SlotDetailDTO {

    private String gameName;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;
    private String myStatus;
}
