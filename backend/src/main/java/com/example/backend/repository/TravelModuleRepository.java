
package com.example.backend.repository;

import com.example.backend.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelModuleRepository extends JpaRepository<Travel, Long> {
}