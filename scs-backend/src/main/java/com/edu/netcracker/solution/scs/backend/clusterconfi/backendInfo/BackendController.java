package com.edu.netcracker.solution.scs.backend.clusterconfi.backendInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/geo-sharding")
@RestController
@CrossOrigin
public class BackendController {

    @Autowired
    private BackendInfoService backendInfoService;


    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody
    BackendInfoDTO getInfo(){
        return backendInfoService.getBackendInfoDTO();
    }
}
