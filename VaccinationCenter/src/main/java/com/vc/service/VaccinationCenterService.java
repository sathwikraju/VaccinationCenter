package com.vc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vc.entity.Citizen;
import com.vc.entity.User;
import com.vc.entity.VaccinationCenter;
import com.vc.repository.CitizenRepository;
import com.vc.repository.UserRepository;
import com.vc.repository.VaccinationCenterRepository;

@Service
public class VaccinationCenterService {
    private final VaccinationCenterRepository vaccinationCenterRepository;
    private final CitizenRepository citizenRepository;
    
    @Autowired
    public VaccinationCenterService(VaccinationCenterRepository vaccinationCenterRepository, UserRepository userRepository, CitizenRepository citizenRepository) {
        this.vaccinationCenterRepository = vaccinationCenterRepository;
        this.citizenRepository = citizenRepository;
    }

	public List<VaccinationCenter> getAllVaccinationCenters() {
		return vaccinationCenterRepository.findAll();
	}

	public VaccinationCenter getVaccinationCenterById(Long id) {
		Optional<VaccinationCenter> center = vaccinationCenterRepository.findById(id);
        return center.orElse(null);
    }

	public List<Citizen> getCitizensByVaccinationCenter(Long centerId) {
		return citizenRepository.findByCenterId(centerId);
    }

	public VaccinationCenter saveVaccinationCenter(VaccinationCenter center) {
        return vaccinationCenterRepository.save(center);
    }

	public boolean deleteVaccinationCenter(Long id) {
        Optional<VaccinationCenter> center = vaccinationCenterRepository.findById(id);
        if (center.isPresent()) {
            vaccinationCenterRepository.delete(center.get());
            return true;
        } else {
            return false;
        }
    }
}