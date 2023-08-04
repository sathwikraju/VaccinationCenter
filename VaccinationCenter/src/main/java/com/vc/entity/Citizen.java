package com.vc.entity;

import javax.persistence.*;


@Entity
@Table(name = "citizen")
public class Citizen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int age;
	
	private int numberOfDoses;
    private String vaccinationStatus;

	@ManyToOne
	@JoinColumn(name = "center_id")
	private VaccinationCenter center;
	
	

	public Citizen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Citizen(Long id, String name, int age, int numberOfDoses, String vaccinationStatus,
			VaccinationCenter center) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.numberOfDoses = numberOfDoses;
		this.vaccinationStatus = vaccinationStatus;
		this.center = center;
	}
	
	public void updateVaccinationStatus() {
        if (numberOfDoses == 0 || numberOfDoses == 1) {
            vaccinationStatus = "Not Vaccinated";
        } else if (numberOfDoses == 2) {
            vaccinationStatus = "Fully Vaccinated";
        } else {
            vaccinationStatus = "Partially Vaccinated";
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public VaccinationCenter getCenter() {
		return center;
	}

	public void setCenter(VaccinationCenter center) {
		this.center = center;
	}

	public int getNumberOfDoses() {
		return numberOfDoses;
	}

	public void setNumberOfDoses(int numberOfDoses) {
		this.numberOfDoses = numberOfDoses;
	}

	public String getVaccinationStatus() {
		return vaccinationStatus;
	}

	public void setVaccinationStatus(String vaccinationStatus) {
		this.vaccinationStatus = vaccinationStatus;
	}

	
}