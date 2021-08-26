package com.edu.netcracker.solution.scs.backend.data.service;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.exception.RestException;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface DataService {

    List<ScsObjectDto> getObjectsInArea(double x1, double x2, double y1, double y2)
            throws RestException;

    String getTypesInArea(double x1, double x2, double y1, double y2);

    ScsObjectDto getObject(String id) throws RestException;

    @NotNull
    String createObject(ScsObjectDto object) throws RestException;

    void deleteObject(String id);

    void updateObjectFields(String id, ScsObjectDto object) throws RestException;

    void updateObject(String id, ScsObjectDto object) throws RestException;
}
