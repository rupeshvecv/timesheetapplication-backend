package com.edcapplication.service;

import com.edcapplication.client.EmpoweredgeClient;
import com.edcapplication.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestBedUserService {

    @Autowired
    private EmpoweredgeClient empoweredgeClient;
    
    public TestBedUserService(EmpoweredgeClient empoweredgeClient) {
        this.empoweredgeClient = empoweredgeClient;
    }

    public List<UserDTO> fetchAllTestBedUsers() {
        try {
            List<UserDTO> users = empoweredgeClient.getAllTestBedUsers();
            System.out.println("✅ TestBed Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public List<UserDTO> fetchAllAttenderUsers() {
        try {
            List<UserDTO> users = empoweredgeClient.getAllAttenderUsers();
            System.out.println("✅ Attender UsersSuccessfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
    
    public List<UserDTO> fetchAllBDRRUsers() {
        try {
            List<UserDTO> users = empoweredgeClient.getAllBDRRUsers();
            System.out.println("✅ BDRR Users Successfully fetched " + users.size() + " users from EmpowerEdge");
            return users;
        } catch (Exception e) {
            System.err.println("❌ Error while calling EmpowerEdge API: " + e.getMessage());
            throw e;
        }
    }
}
