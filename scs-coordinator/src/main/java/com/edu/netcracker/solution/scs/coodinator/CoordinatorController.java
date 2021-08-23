package com.edu.netcracker.solution.scs.coodinator;

import com.edu.netcracker.solution.scs.backend.BackendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geo-sharding")
public class CoordinatorController {

    @Autowired
    private CoordinatorConfigService coordinatorConfigService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody List<BackendInfo> getInfo(){
        return coordinatorConfigService.getInfo();
    }
}
