package com.example.backend.repository;


import com.example.backend.entity.ExpensesDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository
        extends JpaRepository<ExpensesDetail,Integer> {

    List<ExpensesDetail>
    findByEmployeeEmployeeId(Integer empId);

    List<ExpensesDetail>
    findByTravelTravelId(Integer travelId);
}
