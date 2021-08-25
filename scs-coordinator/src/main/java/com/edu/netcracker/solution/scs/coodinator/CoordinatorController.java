package com.edu.netcracker.solution.scs.coodinator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geo-sharding")
@Slf4j
public class CoordinatorController {

    @Autowired
    private CoordinatorConfigService coordinatorConfigService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody List<BackendInfoDTO> getInfo(){
        return coordinatorConfigService.getInfo();
    }
}
