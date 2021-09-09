package com.edu.netcracker.solution.scs.backend.data.controller;

import com.edu.netcracker.solution.scs.backend.data.model.TypesObjectsDto;
import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.data.model.type.ScsTypeDto;
import com.edu.netcracker.solution.scs.backend.data.service.DataService;
import com.edu.netcracker.solution.scs.backend.exception.RestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/v1/data")
@AllArgsConstructor
public class DataController {

    private final DataService dataService;

    /**
     * @return JSON with model objects and types
     */
    @GetMapping(value = "/geo", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TypesObjectsDto> getModelAndObjectsInArea(
            @RequestParam(name = "x1") double x1,
            @RequestParam(name = "x2") double x2,
            @RequestParam(name = "y1") double y1,
            @RequestParam(name = "y2") double y2)
            throws RestException {

        // fetch types
        List<ScsTypeDto> types = dataService.getAllTypes();

        // fetch objects
        List<ScsObjectDto> objects = dataService.getObjectsInArea(x1, x2, y1, y2);

        return new ResponseEntity<>(new TypesObjectsDto(types, objects), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ScsObjectDto> getObject(@PathVariable(name = "id") String id)
            throws RestException {

        return new ResponseEntity<>(dataService.getObject(id), HttpStatus.OK);
    }

    @GetMapping(value = "/model", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ScsTypeDto>> getAllTypes() {
        return new ResponseEntity<>(dataService.getAllTypes(), HttpStatus.OK);
    }

    /**
     * @return JSON {"id": created_id}
     */
    @PostMapping
    ResponseEntity<String> createObject(@Valid @RequestBody ScsObjectDto object)
            throws RestException, JsonProcessingException {

        Map<String, String> restResult = new TreeMap<>();
        restResult.put("id", dataService.createObject(object));

        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(restResult), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteObject(@PathVariable(name = "id") String id) {
        dataService.deleteObject(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateObjectFields(@PathVariable(name = "id") String id, @RequestBody ScsObjectDto object)
            throws RestException {

        dataService.updateObjectFields(id, object);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateObject(@PathVariable(name = "id") String id, @Valid @RequestBody ScsObjectDto object)
            throws RestException {

        dataService.updateObject(id, object);
    }
}
