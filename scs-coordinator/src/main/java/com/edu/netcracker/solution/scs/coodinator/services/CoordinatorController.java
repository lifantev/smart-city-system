package com.edu.netcracker.solution.scs.coodinator.services;


import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geo-sharding")
@Slf4j
public class CoordinatorController {

    @Autowired
    private CoordinatorConfigService coordinatorConfigService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody List<BackendInfoDTO> getInfo(){
        return coordinatorConfigService.getInfo();
    }

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{shard-id}/model",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getModel(@PathVariable("shard-id") String shardId) {
        return coordinatorConfigService.getModel(shardId);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/{shard-id}/objects")
    public @ResponseBody List<ScsObjectDTO> getObjects(@PathVariable("shard-id") String shardId) {
        return coordinatorConfigService.getObjects(shardId);
    }
}
