package com.example.backend.service;

import com.example.backend.dto.request.ApplySlotRequestDTO;
import com.example.backend.dto.request.CancelBookingRequestDTO;
import com.example.backend.dto.response.GameResponseDTO;
import com.example.backend.dto.response.SlotResponseDTO;
import com.example.backend.repository.GameSchedulingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GameSchedulingService {

    private final GameSchedulingRepository repository;

    public GameSchedulingService(GameSchedulingRepository repository) {
        this.repository = repository;
    }

//    @PreAuthorize("hasAnyRole('HR','Employee','Manager')")
    public List<GameResponseDTO> getGames() {
        return repository.getGames();
    }

//    @PreAuthorize("hasAnyRole('HR','Employee','Manager')")
    public List<SlotResponseDTO> getAvailableSlots(int gameId) {
        return repository.getAvailableSlots(gameId);
    }

//    @PreAuthorize("hasAnyRole('HR','Employee','Manager')")
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

}
