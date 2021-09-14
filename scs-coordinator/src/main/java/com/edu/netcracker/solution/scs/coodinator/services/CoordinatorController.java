package com.edu.netcracker.solution.scs.coodinator.services;


import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ClusterDataDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
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
    public @ResponseBody List<ScsTypeDTO> getModel(@PathVariable("shard-id") String shardId) {
        return coordinatorConfigService.getModel(shardId);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/{shard-id}/objects")
    public @ResponseBody List<ScsObjectDTO> getObjects(@PathVariable("shard-id") String shardId) {
        return coordinatorConfigService.getObjects(shardId);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/geo")
    public @ResponseBody List <ClusterDataDTO> showObjects(@RequestParam(value = "x1") double x1, @RequestParam(value = "y1") double y1,
                                                           @RequestParam(value = "x2") double x2, @RequestParam(value = "y2") double y2) throws URISyntaxException {

        log.info("Request params : x1= {{}}, y1 = {{}}, x2 = {{}}, y2 = {{}}", x1, y1, x2, y2);

        return coordinatorConfigService.showObjects(x1, y1, x2, y2);

    }
}
