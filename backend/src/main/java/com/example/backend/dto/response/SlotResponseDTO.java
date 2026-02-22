package com.example.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlotResponseDTO {
    private int slotId;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;
    private String bookedBy;
    private boolean isMySlot;

    public SlotResponseDTO(
            int slotId,
            String slotDate,
            String startTime,
            String endTime,
            String status,
            String bookedBy,
            @JsonProperty("isMySlot")
            boolean isMySlot
    ){
        this.slotId = slotId;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.bookedBy = bookedBy;
        this.isMySlot = isMySlot;
    }

    @JsonProperty("isMySlot")
    public boolean isMySlot() {
        return isMySlot;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public int getSlotId() { return slotId; }
    public String getSlotDate() { return slotDate; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
}