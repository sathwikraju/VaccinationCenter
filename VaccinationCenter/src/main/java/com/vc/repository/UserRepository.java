package com.vc.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vc.entity.*;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
    

    
}