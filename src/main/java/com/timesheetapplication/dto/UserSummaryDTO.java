package com.timesheetapplication.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserSummaryDTO(
        Long id,
        String empCode,
        String userName,
        //String password,
        String profilePic,
        String firstName,
        String middleName,
        String lastName,
        String email,
        String contactNo,
        LocalDateTime originated,
        String functionHead,

        Long statusId,
        Long superiorId,
        Long hrbpId,
        Long departmentId,
        Long designationId,
        Long locationId,

        String statusName,
        String superiorName,
        String hrbpName,
        String departmentName,
        String departmentDescription,
        String designationName,
        String locationName,

        Set<Long> roleIds,
        Set<String> roleNames
        
        
) {

	public Long id() {
		return id;
	}

	public String empCode() {
		return empCode;
	}

	public String userName() {
		return userName;
	}

	/*
	 * public String password() { return password; }
	 */
	public String profilePic() {
		return profilePic;
	}

	public String firstName() {
		return firstName;
	}

	public String middleName() {
		return middleName;
	}

	public String lastName() {
		return lastName;
	}

	public String email() {
		return email;
	}

	public String contactNo() {
		return contactNo;
	}

	public LocalDateTime originated() {
		return originated;
	}

	public Long statusId() {
		return statusId;
	}

	public Long superiorId() {
		return superiorId;
	}

	public Long hrbpId() {
		return hrbpId;
	}

	public Long departmentId() {
		return departmentId;
	}

	public Long designationId() {
		return designationId;
	}

	public Long locationId() {
		return locationId;
	}

	public String statusName() {
		return statusName;
	}

	public String superiorName() {
		return superiorName;
	}

	public String hrbpName() {
		return hrbpName;
	}

	public String departmentName() {
		return departmentName;
	}

	public String designationName() {
		return designationName;
	}

	public String locationName() {
		return locationName;
	}

	public Set<Long> roleIds() {
		return roleIds;
	}

	public Set<String> roleNames() {
		return roleNames;
	}
	
	public String functionHead() {
		return functionHead;
	}

	public String departmentDescription() {
		return departmentDescription;
	}
}

