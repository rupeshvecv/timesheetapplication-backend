package com.timesheetapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "platform", schema = "timesheetapplication")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "platform_name", unique = true)
    private String platformName;

	public Platform() {
		super();
	}

	public Platform(Long id, String platformName) {
		super();
		this.id = id;
		this.platformName = platformName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	@Override
	public String toString() {
		return "Platform [id=" + id + ", platformName=" + platformName + "]";
	}
}

