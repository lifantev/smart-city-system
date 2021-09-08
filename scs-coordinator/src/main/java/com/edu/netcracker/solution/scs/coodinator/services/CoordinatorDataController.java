package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/data")
public class CoordinatorDataController {

    @Autowired
    private CoordinatorDataService dataService;

    @CrossOrigin
    @GetMapping("/{id}")
    public @ResponseBody ScsObjectDTO getObject(@PathVariable String id, HttpServletRequest request) {
        return dataService.getObject(id, request.getRequestURI());
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String createObject(@RequestBody ScsObjectDTO object, HttpServletRequest request) {
        return dataService.createObject(object, request.getRequestURI());
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public void deleteObject(@PathVariable String id, HttpServletRequest request) {
        dataService.deleteObject(id, request.getRequestURI());
    }

    @CrossOrigin
    @PatchMapping("/{id}")
    public void updateObjectFields(@PathVariable String id, @RequestBody ScsObjectDTO object, HttpServletRequest request) {
        dataService.updateObjectFields(id, object, request.getRequestURI());
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public void updateObject(@PathVariable String id, @RequestBody ScsObjectDTO object, HttpServletRequest request) {
        dataService.updateObject(id, object, request.getRequestURI());
    }
}
