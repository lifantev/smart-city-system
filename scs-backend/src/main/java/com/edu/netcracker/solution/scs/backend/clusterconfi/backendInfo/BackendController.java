package com.edu.netcracker.solution.scs.backend.clusterconfi.backendInfo;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.data.model.type.ScsTypeDto;
import com.edu.netcracker.solution.scs.backend.data.service.DataService;
import com.edu.netcracker.solution.scs.backend.exception.RestException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/api/v1/geo-sharding")
@RestController
@CrossOrigin
public class BackendController {

    @Autowired
    private BackendInfoService backendInfoService;
    @Autowired
    private DataService dataService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody
    BackendInfoDTO getInfo(){
        return backendInfoService.getBackendInfoDTO();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/model")
    public ResponseEntity<List<ScsTypeDto>> getModel() {
        return new ResponseEntity<>(dataService.getAllTypes(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/objects")
    public ResponseEntity<List<ScsObjectDto>> getObjects() throws RestException {
        return new ResponseEntity<>(dataService.getAllObjects(), HttpStatus.OK);
    }
}
