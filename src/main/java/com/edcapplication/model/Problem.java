package com.edcapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="problem_table") 
public class Problem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name= "problem_name")
	private String problemName;
	
	@ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
	
	public Problem() {
		super();
	}
	public Problem(Long id, String problemName, Equipment equipment) {
		super();
		this.id = id;
		this.problemName = problemName;
		this.equipment = equipment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProblemName() {
		return problemName;
	}


	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}


	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@Override
	public String toString() {
		return "Problem [id=" + id + ", problemName=" + problemName+ ", equipment=" + equipment +"]";
	}
}
