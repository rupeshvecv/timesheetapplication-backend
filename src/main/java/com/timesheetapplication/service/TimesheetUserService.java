package com.timesheetapplication.service;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.UserSummaryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TimesheetUserService {

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;
    
    public TimesheetUserService(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    public List<UserSummaryDTO> fetchSubordinateUsers(String username) {
        try {
            List<UserSummaryDTO> users = userServiceFeignClient.getSubordinateUsers(username);
            System.out.println("✅ Subordinate Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public String fetchDepartmentByUsername(String username) {
        try {
        	String users = userServiceFeignClient.getDepartmentByUsername(username);
            System.out.println("✅ Department By Username Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public String fetchEmpCodeByUsername(String username) {
        try {
        	String users = userServiceFeignClient.getEmpCodeByUsername(username);
            System.out.println("✅ EMP CODE By Username Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public String fetchFullNameByEmail(String email) {
        try {
        	String users = userServiceFeignClient.getFullNameByEmail(email);
            System.out.println("✅ EMP FullName By email Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public String fetchEmailByUsername(String username) {
        try {
        	String users = userServiceFeignClient.getEmailByUsername(username);
            System.out.println("✅ EMP Email By Username Users Successfully fetched " + users + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public List<UserSummaryDTO> fetchAllOptimizedUsers() {
        try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllOptimizedUsers();
            System.out.println("✅ All Optimized Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public List<UserSummaryDTO> fetchAllOptimizedPDDUsers() {
        try {
            List<UserSummaryDTO> users = userServiceFeignClient.getAllOptimizedPDDUsers();
            System.out.println("✅ AllOptimized PDD Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
}
