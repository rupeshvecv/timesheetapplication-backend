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
@Table(name="subequipment_table") 
public class SubEquipment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name= "subequipment_name")
	private String subequipmentName;

	@ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
	
	public SubEquipment() {
		super();
	}
	
	public SubEquipment(Long id, String subequipmentName, Equipment equipment) {
		super();
		this.id = id;
		this.subequipmentName = subequipmentName;
		this.equipment = equipment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubequipmentName() {
		return subequipmentName;
	}

	public void setSubequipmentName(String subequipmentName) {
		this.subequipmentName = subequipmentName;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@Override
	public String toString() {
		return "SubEquipment [id=" + id + ", subequipmentName=" + subequipmentName + ", equipment=" + equipment+"]";
	}
}
