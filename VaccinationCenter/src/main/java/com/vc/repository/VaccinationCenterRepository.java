package com.vc.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vc.entity.*;

public interface VaccinationCenterRepository extends JpaRepository<VaccinationCenter, Long> {

	List<VaccinationCenter> findByCity(String city);
    
}