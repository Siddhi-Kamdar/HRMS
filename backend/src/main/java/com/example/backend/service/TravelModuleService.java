
package com.example.backend.service;

import com.example.backend.dto.request.TravelRequestDTO;
import com.example.backend.dto.response.TravelResponseDTO;
import com.example.backend.entity.Employee;
import com.example.backend.entity.Travel;
import com.example.backend.entity.TravelEmployee;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.TravelEmployeeRepository;
import com.example.backend.repository.TravelModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
public class TravelModuleService {

    @Autowired
    private TravelModuleRepository travelRepository;

    @Autowired
    private TravelEmployeeRepository travelEmployeeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // GET ALL
    public List<TravelResponseDTO> getAllTravels() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Employee loggedInUser = employeeRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("auth user: "+email);
        System.out.println("authorities : "+authentication.getAuthorities());
        System.out.println("ROLE: "+role);

        if (role.equals("ROLE_HR")) {
            return travelRepository.findAll()
                    .stream()
                    .map(this::mapToDTO)
                    .toList();
        }

        if (role.equals("ROLE_MANAGER")) {

            List<Employee> teamMembers =
                    employeeRepository.findBySupervisor(loggedInUser);

            List<Integer> teamIds = teamMembers
                    .stream()
                    .map(Employee::getEmployeeId)
                    .toList();

            return travelEmployeeRepository.findAll()
                    .stream()
                    .filter(te -> teamIds.contains(
                            te.getEmployee().getEmployeeId()))
                    .map(te -> mapToDTO(te.getTravel()))
                    .distinct()
                    .toList();
        }

        return travelEmployeeRepository.findAll()
                .stream()
                .filter(te -> te.getEmployee()
                        .getEmployeeId()
                        == loggedInUser.getEmployeeId())
                .map(te -> mapToDTO(te.getTravel()))
                .distinct()
                .toList();
    }

    // GET BY ID
    public TravelResponseDTO getTravelById(Long id) {

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        return mapToDTO(travel);
    }

    // CREATE
    public TravelResponseDTO createTravel(TravelRequestDTO request) {

        Employee scheduler = employeeRepository.findById(request.getSchedulerId())
                .orElseThrow(() -> new RuntimeException("Scheduler not found"));

        Travel travel = new Travel();
        travel.setDestination(request.getDestination());
        travel.setDepartDate(request.getDepartDate());
        travel.setReturnDate(request.getReturnDate());
        travel.setScheduler(scheduler);

        Travel savedTravel = travelRepository.save(travel);

        for (Long empId : request.getEmployeeIds()) {

            Employee employee = employeeRepository.findById(empId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            TravelEmployee travelEmployee = new TravelEmployee();
            travelEmployee.setTravel(savedTravel);
            travelEmployee.setEmployee(employee);

            travelEmployeeRepository.save(travelEmployee);
        }

        return mapToDTO(savedTravel);
    }

    // DELETE
    public void deleteTravel(Long id) {

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        travelRepository.delete(travel);
    }

    // MAPPER
    private TravelResponseDTO mapToDTO(Travel travel) {

        List<String> employeeNames =
                travelEmployeeRepository.findByTravel_TravelId(travel.getTravelId())
                        .stream()
                        .map(te -> te.getEmployee().getFullName())
                        .toList();

        List<Integer> employeeIds = travelEmployeeRepository.findByTravel_TravelId(travel.getTravelId())
                .stream()
                .map(te -> te.getEmployee().getEmployeeId())
                .toList();

        return new TravelResponseDTO(
                travel.getTravelId(),
                employeeNames,
                employeeIds,
                travel.getDestination(),
                travel.getDepartDate(),
                travel.getReturnDate()
        );
    }
}