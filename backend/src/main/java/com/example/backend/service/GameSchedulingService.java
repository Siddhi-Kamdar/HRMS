package com.example.backend.service;

import com.example.backend.dto.request.ApplySlotRequestDTO;
import com.example.backend.dto.request.CancelBookingRequestDTO;
import com.example.backend.dto.response.GameResponseDTO;
import com.example.backend.dto.response.SlotDetailDTO;
import com.example.backend.dto.response.SlotResponseDTO;
import com.example.backend.repository.GameSchedulingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GameSchedulingService {

    private final GameSchedulingRepository repository;

    public GameSchedulingService(GameSchedulingRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public List<GameResponseDTO> getGames() {
        return repository.getGames();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public List<SlotResponseDTO> getAvailableSlots(int gameId, int empId) {
        return repository.getAvailableSlots(gameId, empId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public void applyForSlot(ApplySlotRequestDTO request) {

        try{repository.applyForSlot(request);}
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

//    @PreAuthorize("hasAnyRole('HR','Employee','Manager')")
    public void cancelBooking(CancelBookingRequestDTO request) {
        repository.cancelBooking(
                request.getSlotId(),
                request.getCancelledByEmpId()
        );
    }

//    @PreAuthorize("hasAnyRole('HR','Employee','Manager')")
    public void completeSlot(int slotId) {
        repository.completeSlot(slotId);
    }


    public SlotDetailDTO getSlotDetail(
            int slotId,
            int empId){

        return repository.getSlotDetail(slotId, empId);
    }

}
