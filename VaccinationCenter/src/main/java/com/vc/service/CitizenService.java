package com.vc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.vc.entity.Citizen;
import com.vc.repository.CitizenRepository;

@Service
public class CitizenService {
	private final CitizenRepository citizenRepository;

	@Autowired
	public CitizenService(CitizenRepository citizenRepository) {
		this.citizenRepository = citizenRepository;
	}

	// Retrieve all citizens
	public List<Citizen> getAllCitizens() {
		return citizenRepository.findAll();
	}

	// Retrieve a specific citizen by ID
	public Citizen getCitizenById(Long id) {
		return citizenRepository.findById(id).orElse(null);
	}

	public List<Citizen> getCitizensByCenterId(Long centerId) {
		// TODO Auto-generated method stub
		return null;
	}

	// Update a specific citizen
	public Citizen saveCitizen(Citizen updatedCitizen) {
		return citizenRepository.save(updatedCitizen);
    }

	// Delete a specific citizen by ID
    public boolean deleteCitizenById(Long id) {
        try {
            citizenRepository.deleteById(id);
            return true; // Deletion was successful
        } catch (EmptyResultDataAccessException ex) {
            // The citizen with the given ID was not found in the database
            return false;
        } catch (Exception ex) {
            // Other exceptions occurred during deletion
            return false;
        }
    }}
