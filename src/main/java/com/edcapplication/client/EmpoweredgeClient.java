package com.edcapplication.client;

import com.edcapplication.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(
        name = "empoweredgeClient",
        url = "${empoweredge.api.base-url}",
        configuration = com.edcapplication.config.FeignAuthInterceptor.class
)
public interface EmpoweredgeClient {
	
    @GetMapping("/api/empoweredge/testbedusers")
    List<UserDTO> getAllTestBedUsers();

    @GetMapping("/api/empoweredge/attenderusers")
    List<UserDTO> getAllAttenderUsers();
    
    @GetMapping("/api/empoweredge/bdrrusers")
    List<UserDTO> getAllBDRRUsers();
}
