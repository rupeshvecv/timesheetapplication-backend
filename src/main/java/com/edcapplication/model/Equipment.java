package com.edcapplication.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="equipment_table") 
public class Equipment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name= "equipment_name")
	private String equipmentName;

	/*
	 * @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval =
	 * true) private List<SubEquipment> subEquipments;
	 * 
	 * @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval =
	 * true) private List<Problem> problems;
	 */
	
	public Equipment() {
		super();
	}
	/*
	 * public Equipment(int id, String equipmentName, List<SubEquipment>
	 * subEquipments, List<Problem> problems) { super(); this.id = id;
	 * this.equipmentName = equipmentName; this.subEquipments = subEquipments;
	 * this.problems = problems; }
	 */
	
	public Equipment(Long id, String equipmentName) {
		super();
		this.id = id;
		this.equipmentName = equipmentName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	/*
	 * public List<SubEquipment> getSubEquipments() { return subEquipments; }
	 * 
	 * public void setSubEquipments(List<SubEquipment> subEquipments) {
	 * this.subEquipments = subEquipments; }
	 * 
	 * public List<Problem> getProblems() { return problems; }
	 * 
	 * public void setProblems(List<Problem> problems) { this.problems = problems; }
	 */

	/*
	 * @Override public String toString() { return "Equipment [id=" + id +
	 * ", equipmentName=" + equipmentName+ ", subEquipments=" + subEquipments+
	 * ", problems=" + problems +"]"; }
	 */
	
	@Override
	public String toString() {
		return "Equipment [id=" + id + ", equipmentName=" + equipmentName +"]";
	}
}
