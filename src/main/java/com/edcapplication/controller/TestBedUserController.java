package com.edcapplication.controller;

import com.edcapplication.dto.UserDTO;
import com.edcapplication.service.TestBedUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/edcapplication")
public class TestBedUserController {

    private final TestBedUserService service;

    public TestBedUserController(TestBedUserService service) {
        this.service = service;
    }

    @GetMapping("/testbedusers")
    public List<UserDTO> getAllTestBedUsers() {
        return service.fetchAllTestBedUsers();
    }
    
    @GetMapping("/attenderusers")
    public List<UserDTO> getAllAttenderUsers(){
        return service.fetchAllAttenderUsers();
    }
    
    @GetMapping("/bdrrusers")
    public List<UserDTO> getAllBDRRUsers(){
        return service.fetchAllBDRRUsers();
    }
}
