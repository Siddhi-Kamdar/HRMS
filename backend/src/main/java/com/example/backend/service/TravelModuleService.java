
package com.example.backend.service;

import com.example.backend.dto.request.TravelRequestDTO;
import com.example.backend.dto.response.TravelResponseDTO;
import com.example.backend.entity.Employee;
import com.example.backend.entity.Travel;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.TravelModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelModuleService {

    @Autowired
    private TravelModuleRepository travelRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // ------------ get all ---------------
    public List<TravelResponseDTO> getAllTravels() {

        return travelRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ------------ get by id  ---------------
    public TravelResponseDTO getTravelById(Long id) {

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        return mapToDTO(travel);
    }

    // ------------ create ---------------
    public TravelResponseDTO createTravel(TravelRequestDTO request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Travel travel = new Travel();
        travel.setEmployee(employee);
        travel.setDestination(request.getDestination());
        travel.setDepartDate(request.getDepartDate());
        travel.setReturnDate(request.getReturnDate());


        Travel saved = travelRepository.save(travel);

        return mapToDTO(saved);
    }

    // ------------ update ---------------
    public TravelResponseDTO updateTravel(Long id, TravelRequestDTO request) {

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        travel.setEmployee(employee);
        travel.setDestination(request.getDestination());
        travel.setDepartDate(request.getDepartDate());
        travel.setReturnDate(request.getReturnDate());

        Travel updated = travelRepository.save(travel);

        return mapToDTO(updated);
    }

    // ------------ delete ---------------
    public void deleteTravel(Long id) {

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        travelRepository.delete(travel);
    }


    private TravelResponseDTO mapToDTO(Travel travel) {
        return new TravelResponseDTO(
                travel.getTravelId(),
                travel.getEmployee().getFullName(),
                travel.getDestination(),
                travel.getDepartDate(),
                travel.getReturnDate()
        );
    }
}
