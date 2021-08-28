package com.edu.netcracker.solution.scs.coodinator.services;


import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import com.edu.netcracker.solution.scs.coodinator.services.CoordinatorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geo-sharding")
@Slf4j
@CrossOrigin
public class CoordinatorController {

    @Autowired
    private CoordinatorConfigService coordinatorConfigService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody List<BackendInfoDTO> getInfo(){
        return coordinatorConfigService.getInfo();
    }
}
