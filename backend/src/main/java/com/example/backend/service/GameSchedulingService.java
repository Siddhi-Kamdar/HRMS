package com.example.backend.service;

import com.example.backend.dto.request.ApplySlotRequestDTO;
import com.example.backend.dto.request.CancelBookingRequestDTO;
import com.example.backend.dto.response.GameResponseDTO;
import com.example.backend.dto.response.SlotDetailDTO;
import com.example.backend.dto.response.SlotResponseDTO;
import com.example.backend.entity.Employee;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.GameSchedulingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GameSchedulingService {

    private final GameSchedulingRepository repository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private final EmployeeRepository employeeRepository;

    public GameSchedulingService(
            GameSchedulingRepository repository,
            NotificationService notificationService,
            EmployeeRepository employeeRepository) {

        this.repository = repository;
        this.notificationService = notificationService;
        this.employeeRepository = employeeRepository;
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

        try {

            repository.applyForSlot(request);

            Employee leader =
                    employeeRepository
                            .findById((long) request.getLeaderEmpId())
                            .orElseThrow();

            String leaderName = leader.getFullName();

            SlotDetailDTO slot =
                    repository.getSlotDetail(
                            request.getSlotId(),
                            request.getLeaderEmpId()
                    );

            List<String> names =
                    request.getMembers()
                            .stream()
                            .map(id ->
                                    employeeRepository
                                            .findById(id.longValue())
                                            .orElseThrow()
                                            .getFullName())
                            .toList();

            String players =
                    String.join(", ", names);

            LocalTime time =
                    LocalTime.parse(slot.getStartTime());

            String formattedTime =
                    time.format(DateTimeFormatter.ofPattern("h:mm a"));

            String message =
                    leaderName +
                            " booked " +
                            slot.getGameName() +
                            " with " +
                            players +
                            " at " +
                            formattedTime;

            request.getMembers()
                    .stream()
                    .filter(id -> !id.equals(request.getLeaderEmpId()))
                    .forEach(empId -> {

                        Employee member =
                                employeeRepository
                                        .findById(empId.longValue())
                                        .orElseThrow();

                        notificationService.create(
                                member,
                                "Game Slot Booked",
                                message
                        );

                    });

        } catch (Exception e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public void cancelBooking(CancelBookingRequestDTO request) {
        repository.cancelBooking(
                request.getSlotId(),
                request.getCancelledByEmpId()
        );
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public void completeSlot(int slotId) {
        repository.completeSlot(slotId);
    }


    public SlotDetailDTO getSlotDetail(
            int slotId,
            int empId){

        return repository.getSlotDetail(slotId, empId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR')")
    public void enableSlot(int slotId){
        repository.enableSlot(slotId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR')")
    public void disableSlot(int slotId){
        repository.disableSlot(slotId);
    }

    public void generateSlots() {
        repository.generateSlots();
    }
}
