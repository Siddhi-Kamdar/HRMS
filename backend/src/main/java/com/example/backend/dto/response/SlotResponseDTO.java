package com.example.backend.dto.response;

public class SlotResponseDTO {
    private int slotId;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;

    public SlotResponseDTO(int slotId, String slotDate,
                           String startTime, String endTime,
                           String status) {
        this.slotId = slotId;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getSlotId() { return slotId; }
    public String getSlotDate() { return slotDate; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
}