package com.edu.netcracker.solution.scs.backend.data.service;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.exception.RestException;

public interface DataValidationService {

    void validateObjectForSaving(ScsObjectDto object) throws RestException;

    void validateType(String type) throws RestException;

    void validateGeoPosition(double geoPosX, double geoPosY) throws RestException;

    void validateFieldConstraints(ScsObjectDto object) throws RestException;
}
