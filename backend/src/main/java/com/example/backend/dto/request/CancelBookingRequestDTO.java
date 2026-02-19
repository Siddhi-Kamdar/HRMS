package com.example.backend.dto.request;

public class CancelBookingRequestDTO {

    private int slotId;
    private int cancelledByEmpId;

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getCancelledByEmpId() {
        return cancelledByEmpId;
    }

    public void setCancelledByEmpId(int cancelledByEmpId) {
        this.cancelledByEmpId = cancelledByEmpId;
    }
}