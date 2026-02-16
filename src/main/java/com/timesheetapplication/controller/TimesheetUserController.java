package com.timesheetapplication.controller;

import com.timesheetapplication.dto.DepartmentDTO;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.service.TimesheetUserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/timesheetapplication")
public class TimesheetUserController {

    private final TimesheetUserService timesheetUserService;

    public TimesheetUserController(TimesheetUserService service) {
        this.timesheetUserService = service;
    }

    @GetMapping("/users/subordinatelist/{username}")
    public ResponseEntity<List<UserSummaryDTO>> getSubordinateUsers(@PathVariable("username") String username) {
    	List<UserSummaryDTO> users = timesheetUserService.fetchSubordinateUsers(username);
         return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/getDepartmentByUsername/{username}")
    public ResponseEntity<String> getDepartmentByUsername(@PathVariable("username") String username) {
        String departmentName = timesheetUserService.fetchDepartmentByUsername(username);
        if (departmentName == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departmentName);
    }
    
    @GetMapping("/users/getEmpCodeByUsername/{username}")
    public ResponseEntity<String> getEmpCodeByUsername(@PathVariable("username") String username) {
        String empCode = timesheetUserService.fetchEmpCodeByUsername(username);
        if (empCode == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empCode);
    }
    
    @GetMapping("/users/fullNameByEmail/{email}")
    public ResponseEntity<String> getFullNameByEmail(@PathVariable("email") String email) {
        String fullName = timesheetUserService.fetchFullNameByEmail(email);
        if (fullName == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fullName);
    }
    
    @GetMapping("/users/getEmailByUsername/{username}")
    public ResponseEntity<String> getEmailByUsername(@PathVariable("username") String username) {
        String email = timesheetUserService.fetchEmailByUsername(username);
        if (email == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(email);
    }
    
    @GetMapping("/allusers")
    public List<UserSummaryDTO> getAllOptimizedUsers(){
        return timesheetUserService.fetchAllOptimizedUsers();
    }
    
    @GetMapping("/allpddusers")
    public List<UserSummaryDTO> getAllOptimizedPDDUsers(){
        return timesheetUserService.fetchAllOptimizedPDDUsers();
    }
    
    @GetMapping("/functionhead")
    public List<UserSummaryDTO> getAllFunctionHead(){
        return timesheetUserService.fetchAllFunctionHead();
    }
    
    @GetMapping("/departments")
    public List<DepartmentDTO> getDepartments(){
        return timesheetUserService.fetchAllDepartments();
    }
    
    @GetMapping("/pdddepartments")
    public List<DepartmentDTO> getPDDDepartments(){
        return timesheetUserService.fetchAllPDDDepartments();
    }
    
}
