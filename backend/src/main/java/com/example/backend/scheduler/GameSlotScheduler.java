package com.example.backend.scheduler;

import com.example.backend.service.GameSchedulingService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class GameSlotScheduler {

    private final GameSchedulingService service;

    public GameSlotScheduler(GameSchedulingService service) {
        this.service = service;
    }

    @PostConstruct
    public void runOnStartup() {
        service.generateSlots();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateSlotsDaily() {
        service.generateSlots();
    }
}