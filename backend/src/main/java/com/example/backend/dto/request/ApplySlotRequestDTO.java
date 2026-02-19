package com.example.backend.dto.request;

import java.util.List;

public class ApplySlotRequestDTO {

    private int slotId;
    private int leaderEmpId;
    private List<Integer> members;

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getLeaderEmpId() {
        return leaderEmpId;
    }

    public void setLeaderEmpId(int leaderEmpId) {
        this.leaderEmpId = leaderEmpId;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }
}