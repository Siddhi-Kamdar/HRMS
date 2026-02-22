package com.example.backend.controller;

import com.example.backend.dto.request.ApplySlotRequestDTO;
import com.example.backend.dto.request.CancelBookingRequestDTO;
import com.example.backend.dto.response.GameResponseDTO;
import com.example.backend.dto.response.SlotResponseDTO;
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
    public List<GameResponseDTO> getGames() {
        return service.getGames();
    }

    @GetMapping("/{gameId}/slots")
    public List<SlotResponseDTO> getSlots(@PathVariable int gameId, @RequestParam int empId) {
        return service.getAvailableSlots(gameId, empId);
    }

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody ApplySlotRequestDTO request) {

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
    public ResponseEntity<String> cancel(@RequestBody CancelBookingRequestDTO request) {
        service.cancelBooking(request);
        return ResponseEntity.ok("Cancelled successfully");
    }

    @PostMapping("/{slotId}/complete")
    public ResponseEntity<String> complete(@PathVariable int slotId) {
        service.completeSlot(slotId);
        return ResponseEntity.ok("Slot completed successfully");
    }

}