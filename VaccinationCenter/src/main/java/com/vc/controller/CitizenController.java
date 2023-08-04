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
import org.springframework.web.servlet.ModelAndView;

import com.vc.entity.Citizen;
import com.vc.entity.VaccinationCenter;
import com.vc.repository.CitizenRepository;
import com.vc.repository.VaccinationCenterRepository;
import com.vc.service.CitizenService;

@Controller
public class CitizenController {
	private final CitizenService citizenService;
	private final CitizenRepository citizenRepository;
	private final VaccinationCenterRepository vaccinationCenterRepository;

	@Autowired
	public CitizenController(CitizenService citizenService, CitizenRepository citizenRepository,
			VaccinationCenterRepository vaccinationCenterRepository) {
		this.citizenService = citizenService;
		this.citizenRepository = citizenRepository;
		this.vaccinationCenterRepository = vaccinationCenterRepository;
	}

	@GetMapping("/addCitizen")
	public String showAddCitizenForm(Model model) {
		List<VaccinationCenter> vaccinationCenters = vaccinationCenterRepository.findAll();
		model.addAttribute("vaccinationCenters", vaccinationCenters);
		model.addAttribute("citizen", new Citizen());
		return "addcitizenform";
	}

	@PostMapping("/addCitizen")
	public String processAddCitizenForm(@ModelAttribute Citizen citizen) {
		VaccinationCenter center = vaccinationCenterRepository.findById(citizen.getCenter().getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid center id: " + citizen.getCenter().getId()));

		citizen.setCenter(center);

		// Determine vaccination status based on the number of doses
		if (citizen.getNumberOfDoses() == 0 || citizen.getNumberOfDoses() == 1) {
			citizen.setVaccinationStatus("Not Vaccinated");
		} else if (citizen.getNumberOfDoses() == 2) {
			citizen.setVaccinationStatus("Fully Vaccinated");
		} else {
			// Handle other cases if needed
			citizen.setVaccinationStatus("Unknown");
		}

		citizenRepository.save(citizen);
		return "redirect:/citizens";
	}

	@GetMapping("/viewCitizen/{id}")
	public String viewCitizen(@PathVariable Long id, Model model) {
		Citizen citizen = citizenRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid citizen id: " + id));

		model.addAttribute("citizen", citizen);
		return "viewcitizen";
	}

	@GetMapping("/editCitizen/{id}")
	public String editCitizenForm(@PathVariable Long id, Model model) {
		Citizen citizen = citizenRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid citizen id: " + id));

		List<VaccinationCenter> vaccinationCenters = vaccinationCenterRepository.findAll();

		model.addAttribute("citizen", citizen);
		model.addAttribute("vaccinationCenters", vaccinationCenters);

		return "editcitizen";
	}

	@PostMapping("/editCitizen/{id}")
	public String updateCitizen(@PathVariable Long id, @ModelAttribute Citizen updatedCitizen) {
		Citizen citizen = citizenRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid citizen id: " + id));

		citizen.setName(updatedCitizen.getName());
		citizen.setAge(updatedCitizen.getAge());
		citizen.setNumberOfDoses(updatedCitizen.getNumberOfDoses());
		citizen.setVaccinationStatus(updatedCitizen.getVaccinationStatus());

		// Update vaccination status based on the number of doses
		if (updatedCitizen.getNumberOfDoses() == 2) {
			citizen.setVaccinationStatus("Fully Vaccinated");
		} else if (updatedCitizen.getNumberOfDoses() == 1) {
			citizen.setVaccinationStatus("Partially Vaccinated");
		} else {
			citizen.setVaccinationStatus("Not Vaccinated");
		}

		// Update the associated VaccinationCenter using the centerId from the form
		Long centerId = updatedCitizen.getCenter().getId();
		VaccinationCenter center = vaccinationCenterRepository.findById(centerId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid center id: " + centerId));
		citizen.setCenter(center);

		citizenRepository.save(citizen);

		return "redirect:/citizens";
	}

	@GetMapping("/deleteCitizen/{id}")
	public ModelAndView deleteCitizen(@PathVariable Long id) {
		Citizen citizen = citizenRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid citizen id: " + id));

		citizenRepository.delete(citizen);
		return new ModelAndView("redirect:/citizens");
	}

	// Retrieve all citizens
	@GetMapping("/citizens")
	public String viewCitizens(Model model) {
		List<Citizen> citizens = citizenRepository.findAll();
		model.addAttribute("citizens", citizens);
		return "citizens";
	}

}
