package com.timesheetapplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.DepartmentDTO;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.exception.BusinessException;

import feign.FeignException;

@Service
public class TimesheetUserService {

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;
    
    public TimesheetUserService(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    public List<UserSummaryDTO> fetchSubordinateUsers(String username) {
       /* try {
            List<UserSummaryDTO> users = userServiceFeignClient.getSubordinateUsers(username);
            System.out.println("✅ Subordinate Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	
    	//1️.Validate input
        if (username == null || username.isBlank()) {
            throw new BusinessException("USR_001");
        }

        try {
            List<UserSummaryDTO> users = userServiceFeignClient.getSubordinateUsers(username);

            //2️.Handle empty response
            if (users == null || users.isEmpty()) {
                throw new BusinessException("USR_005", username);
            }
            System.out.println("✅ Subordinate Users Successfully fetched " + users.size() + " users from EmpowerEdge");

            return users;

        } catch (FeignException.NotFound ex) {
            //3️.User not found
            throw new BusinessException("USR_005", username);

        } catch (FeignException ex) {
            //4️.Feign / service failure
        	System.err.println("❌ Error while calling EmpowerEdge API: " + ex);
            throw new BusinessException("USR_003");
        }
    }
    
    public String fetchDepartmentByUsername(String username) {
      /*  try {
        	String users = userServiceFeignClient.getDepartmentByUsername(username);
            System.out.println("✅ Department By Username Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	
    	//1️.Validate input
        if (username == null || username.isBlank()) {
            throw new BusinessException("USR_001");
        }
        try {
            String department = userServiceFeignClient.getDepartmentByUsername(username);

            //2️.Handle empty response
            if (department == null || department.isBlank()) {
                throw new BusinessException("USR_002", username);
            }
            System.err.println("Department fetched successfully for user {}: " + username);
            return department;

        } catch (FeignException.NotFound ex) {
            //3️.User not found
            throw new BusinessException("USR_002", username);

        } catch (FeignException ex) {
            //4️.User service failure
            System.err.println("Error while fetching department for {} {}: " + ex);
            throw new BusinessException("USR_006");
        }
    }
    
    public String fetchEmpCodeByUsername(String username) {
        /*try {
        	String users = userServiceFeignClient.getEmpCodeByUsername(username);
            System.out.println("✅ EMP CODE By Username Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	//1️.Validate input
        if (username == null || username.isBlank()) {
            throw new BusinessException("USR_001");
        }
        try {
            String empCode = userServiceFeignClient.getEmpCodeByUsername(username);

            //2️.Handle empty response
            if (empCode == null || empCode.isBlank()) {
                throw new BusinessException("USR_004", username);
            }
            System.out.println("✅ EMP CODE By Username Users Successfully fetched " + empCode + " users from EmpowerEdge");
            return empCode;

        } catch (FeignException.NotFound ex) {
            //3️.User not found in remote service
            throw new BusinessException("USR_004", username);

        } catch (FeignException ex) {
            //4️.Remote service failure
        	System.err.println("❌ Error while calling EmpowerEdge API: " + ex);
            throw new BusinessException("USR_007");
        }
    }
    
    public String fetchFullNameByEmail(String email) {
        /*try {
        	String users = userServiceFeignClient.getFullNameByEmail(email);
            System.out.println("✅ EMP FullName By email Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	
    	//1️.Validate input
        if (email == null || email.isBlank()) {
            throw new BusinessException("USR_008");
        }

        try {
            String fullName = userServiceFeignClient.getFullNameByEmail(email);

            //2️.Handle empty response
            if (fullName == null || fullName.isBlank()) {
                throw new BusinessException("USR_009", email);
            }
            return fullName;

        } catch (FeignException.NotFound ex) {
            //3️.Email not found in remote system
            throw new BusinessException("USR_009", email);

        } catch (FeignException ex) {
            //4️.Remote service error
        	System.err.println("❌ Error while calling EmpowerEdge API: " + ex);
            throw new BusinessException("USR_010");
        }
    }
    
    public String fetchEmailByUsername(String username) {
        /*try {
        	String users = userServiceFeignClient.getEmailByUsername(username);
            System.out.println("✅ EMP Email By Username Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	//1️.Validate input
        if (username == null || username.isBlank()) {
            throw new BusinessException("USR_001");
        }
        try {
            String email = userServiceFeignClient.getEmailByUsername(username);

            //2️.Handle empty response
            if (email == null || email.isBlank()) {
                throw new BusinessException("USR_011", username);
            }
            return email;

        } catch (FeignException.NotFound ex) {
            //3️.User not found
            throw new BusinessException("USR_011", username);

        } catch (FeignException ex) {
            //4️.Remote service error
            System.err.println("Error while fetching email for {} " + ex);
            throw new BusinessException("USR_012");
        }
    }
    
    public List<UserSummaryDTO> fetchAllOptimizedUsers() {
        /*try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllOptimizedUsers();
            System.out.println("✅ All Optimized Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	
    	try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllOptimizedUsers();

            //1️.Handle empty response
            if (users == null || users.isEmpty()) {
                throw new BusinessException("USR_013");
            }
            System.out.println("All Optimized Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;

        } catch (FeignException ex) {
            //2️.Remote service failure
            System.err.println("Error while fetching optimized users " + ex);
            throw new BusinessException("USR_014");
        }
    }
    
    public List<UserSummaryDTO> fetchAllOptimizedPDDUsers() {
        /*try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllOptimizedPDDUsers();
            System.out.println("✅ AllOptimized PDD Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }*/
    	
    	try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllOptimizedPDDUsers();

            //1️.Handle empty response
            if (users == null || users.isEmpty()) {
                throw new BusinessException("USR_013");
            }
            System.out.println("All PDD Optimized Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;

        } catch (FeignException ex) {
            //2️.Remote service failure
            System.err.println("Error while fetching optimized users " + ex);
            throw new BusinessException("USR_014");
        }
    }
    
    public List<UserSummaryDTO> fetchAllFunctionHead() {
    	try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllFunctionHead();

            if (users == null || users.isEmpty()) {
                throw new BusinessException("USR_013");
            }
            System.out.println("✅ Successfully fetched " + users.size() + " Function Heads from EmpowerEdge");
            return users;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("❌ Error calling EmpowerEdge API: " + ex.getMessage());
            throw new BusinessException("USR_014");
        }
    }
    
    public List<DepartmentDTO> fetchAllDepartments() {
    	try {
            List<DepartmentDTO> departments = userServiceFeignClient.getDepartments();

            if (departments == null || departments.isEmpty()) {
                throw new BusinessException("USR_015");
            }
            System.out.println("✅ Successfully fetched " + departments.size() + " Departmentss from EmpowerEdge");
            return departments;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("❌ Error calling EmpowerEdge API: " + ex.getMessage());
            throw new BusinessException("USR_016");
        }
    }
    
    public List<DepartmentDTO> fetchAllPDDDepartments() {
    	try {
            List<DepartmentDTO> departments = userServiceFeignClient.getPDDDepartments();

            if (departments == null || departments.isEmpty()) {
                throw new BusinessException("USR_015");
            }
            System.out.println("✅ Successfully fetched " + departments.size() + " PDD Departmentss from EmpowerEdge");
            return departments;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("❌ Error calling EmpowerEdge API: " + ex.getMessage());
            throw new BusinessException("USR_016");
        }
    }
}
