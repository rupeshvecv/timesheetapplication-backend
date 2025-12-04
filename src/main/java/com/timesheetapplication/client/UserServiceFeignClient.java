package com.timesheetapplication.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.timesheetapplication.dto.UserSummaryDTO;

@FeignClient(
        name = "empoweredgeClient",
        url = "${empoweredge.api.base-url}",
        configuration = com.timesheetapplication.config.FeignAuthInterceptor.class
)
public interface UserServiceFeignClient {
	
    @GetMapping("/api/empoweredge/users/subordinatelist/{username}")
    List<UserSummaryDTO> getSubordinateUsers(@PathVariable("username") String username);

    @GetMapping("/api/empoweredge/users/getDepartmentByUsername/{username}")
    String getDepartmentByUsername(@PathVariable("username") String username);
    
    @GetMapping("/api/empoweredge/users/getEmpCodeByUsername/{username}")
    String getEmpCodeByUsername(@PathVariable("username") String username);
    
    @GetMapping("/api/empoweredge/users/fullNameByEmail/{email}")
    String getFullNameByEmail(@PathVariable("email") String email);
    
    @GetMapping("/api/empoweredge/users/getEmailByUsername/{username}")
    String getEmailByUsername(@PathVariable("username") String username);
    
    @GetMapping("/api/empoweredge/allusers")
    List<UserSummaryDTO> getAllOptimizedUsers();
    
    @GetMapping("/api/empoweredge/allpddusers")
    List<UserSummaryDTO> getAllOptimizedPDDUsers();
}
