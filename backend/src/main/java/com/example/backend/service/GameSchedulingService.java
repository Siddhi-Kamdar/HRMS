package com.example.backend.service;

import com.example.backend.dto.request.ApplySlotRequest;
import com.example.backend.dto.request.CancelBookingRequest;
import com.example.backend.dto.response.GameResponse;
import com.example.backend.dto.response.SlotResponse;
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

    public List<GameResponse> getGames() {
        return repository.getGames();
    }

    public List<SlotResponse> getAvailableSlots(int gameId) {
        return repository.getAvailableSlots(gameId);
    }

    public void applyForSlot(ApplySlotRequest request) {

        try{repository.applyForSlot(request);}
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    public void cancelBooking(CancelBookingRequest request) {
        repository.cancelBooking(
                request.getSlotId(),
                request.getCancelledByEmpId()
        );
    }

    public void completeSlot(int slotId) {
        repository.completeSlot(slotId);
    }

}
