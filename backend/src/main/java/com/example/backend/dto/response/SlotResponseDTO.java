package com.example.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SlotResponseDTO {
    private int slotId;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;
    private String bookedBy;
    private boolean isMySlot;
    @JsonProperty("isMySlot")
    public boolean isMySlot() {
        return isMySlot;
    }


}