package com.vc.entity;

import javax.persistence.*;

@Entity
@Table(name = "vaccination_center")
public class VaccinationCenter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String city;
	
	

	public VaccinationCenter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VaccinationCenter(Long id, String name, String city) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}