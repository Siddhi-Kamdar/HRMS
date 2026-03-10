package com.example.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelBookingRequestDTO {

    private int slotId;
    private int cancelledByEmpId;

}