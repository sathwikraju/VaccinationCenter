package com.vc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vc.service.VaccinationCenterService;
import com.vc.entity.*;
import com.vc.repository.CitizenRepository;
import com.vc.repository.VaccinationCenterRepository;

@Controller
public class VaccinationCenterController {
	private final VaccinationCenterService vaccinationCenterService;

	private final CitizenRepository citizenRepository;
	private final VaccinationCenterRepository vaccinationCenterRepository;

	@Autowired
	public VaccinationCenterController(CitizenRepository citizenRepository,
			VaccinationCenterRepository vaccinationCenterRepository,
			VaccinationCenterService vaccinationCenterService) {
		this.vaccinationCenterService = vaccinationCenterService;
		this.citizenRepository = citizenRepository;
		this.vaccinationCenterRepository = vaccinationCenterRepository;
	}

	@GetMapping("/getVaccinationCenters")
	@ResponseBody
	public List<VaccinationCenter> getVaccinationCentersByCity(@RequestParam String city) {
		// Fetch the vaccination centers in the selected city from the database
		List<VaccinationCenter> centers = vaccinationCenterRepository.findByCity(city);
		return centers;
	}

	@GetMapping("/addCenterForm")
	public String showAddUserForm(Model model) {
		model.addAttribute("vaccinationcenter", new VaccinationCenter());
		return "addcenterform";
	}

	@PostMapping("/addCenter")
	public String processAddUserForm(@ModelAttribute VaccinationCenter center) {
		vaccinationCenterRepository.save(center);
		return "redirect:/vaccinationCenters";
	}

	@GetMapping("/vaccinationCenters")
	public String viewVaccinationCenters(Model model) {
		List<VaccinationCenter> vaccinationCenters = vaccinationCenterRepository.findAll();
		model.addAttribute("vaccinationCenters", vaccinationCenters);
		return "vaccinationcenters";
	}

	@GetMapping("/vaccinationcenter/{id}")
	public String viewCenter(@PathVariable Long id, Model model) {
		VaccinationCenter center = vaccinationCenterRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid center id: " + id));
		List<Citizen> citizens = citizenRepository.findByCenterId(id);

		model.addAttribute("center", center);
		model.addAttribute("citizens", citizens);
		return "viewcenter";
	}

	@GetMapping("/vaccinationcenter/citizens/{centerId}")
	public ResponseEntity<List<Citizen>> getCitizensByVaccinationCenter(@PathVariable Long centerId) {
		List<Citizen> citizens = vaccinationCenterService.getCitizensByVaccinationCenter(centerId);
		if (citizens.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(citizens);
	}

	// edit form for vaccination center
	@GetMapping("/editCenter/{id}")
	public String editCenter(@PathVariable Long id, Model model) {
		VaccinationCenter center = vaccinationCenterRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid center id: " + id));

		model.addAttribute("center", center);
		return "editcenter";
	}

	// editing the vaccination center details
	@PostMapping("/editCenter/{id}")
	public String updateCenter(@PathVariable Long id, @ModelAttribute VaccinationCenter updatedCenter) {
		VaccinationCenter center = vaccinationCenterRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid center id: " + id));

		center.setName(updatedCenter.getName());
		center.setCity(updatedCenter.getCity());
		vaccinationCenterRepository.save(center);

		return "redirect:/vaccinationCenters";
	}

	@GetMapping("/deleteCenter/{id}")
	public String deleteVaccinationCenter(@PathVariable Long id) {
		// Find the vaccination center to delete
		VaccinationCenter center = vaccinationCenterRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid center id: " + id));

		// Update the center_id to null for associated citizen records
		List<Citizen> citizensWithCenter = citizenRepository.findByCenterId(id);
		for (Citizen citizen : citizensWithCenter) {
			citizen.setCenter(null);
			citizenRepository.save(citizen);
		}

		// Delete the vaccination center
		vaccinationCenterRepository.deleteById(id);

		return "redirect:/vaccinationCenters";
	}
}