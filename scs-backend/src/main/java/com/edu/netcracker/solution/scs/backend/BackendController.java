package com.edu.netcracker.solution.scs.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/geo-sharding")
@RestController
public class BackendController {

    @Autowired
    private BackendInfo backendInfo;


    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody
    BackendInfo getInfo(){
        return this.backendInfo;
    }
}
