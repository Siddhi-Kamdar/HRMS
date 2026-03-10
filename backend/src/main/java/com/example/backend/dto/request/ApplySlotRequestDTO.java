package com.example.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplySlotRequestDTO {

    private int slotId;
    private int leaderEmpId;
    private List<Integer> members;
}