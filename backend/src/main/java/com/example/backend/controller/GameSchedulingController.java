package com.example.backend.controller;

import com.example.backend.dto.request.ApplySlotRequest;
import com.example.backend.dto.request.CancelBookingRequest;
import com.example.backend.dto.response.GameResponse;
import com.example.backend.dto.response.SlotResponse;
import com.example.backend.service.GameSchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameSchedulingController {

    private final GameSchedulingService service;

    public GameSchedulingController(GameSchedulingService service) {
        this.service = service;
    }

    @GetMapping
    public List<GameResponse> getGames() {
        return service.getGames();
    }

    @GetMapping("/{gameId}/slots")
    public List<SlotResponse> getSlots(@PathVariable int gameId) {
        return service.getAvailableSlots(gameId);
    }

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody ApplySlotRequest request) {

        try{
            service.applyForSlot(request);
            return ResponseEntity.ok("Applied successfully");
        }
        catch(Exception e){
            Throwable root = e.getCause();
            while (root != null && root.getCause() != null){
                root = root.getCause();
            }
            String message = root != null ? root.getMessage() : e.getMessage();

            return ResponseEntity.badRequest().body(message);
        }

    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestBody CancelBookingRequest request) {
        service.cancelBooking(request);
        return ResponseEntity.ok("Cancelled successfully");
    }

    @PostMapping("/{slotId}/complete")
    public ResponseEntity<String> complete(@PathVariable int slotId) {
        service.completeSlot(slotId);
        return ResponseEntity.ok("Slot completed successfully");
    }

}