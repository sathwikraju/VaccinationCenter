package com.vc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vc.entity.Citizen;
import com.vc.entity.User;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

	List<Citizen> findByCenterId(Long centerId);
    
}
