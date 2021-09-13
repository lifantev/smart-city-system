package com.edu.netcracker.solution.scs.backend.data.service;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.data.model.type.ScsTypeDto;
import com.edu.netcracker.solution.scs.backend.data.model.type.TypeRepository;
import com.edu.netcracker.solution.scs.backend.exception.RestException;
import com.edu.netcracker.solution.scs.backend.exception.RestExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DataValidationServiceImpl implements DataValidationService {

    private final TypeRepository typeRepository;
    private final GeoValidationService geoValidationService;

    /**
     * Provides all possible validations for model object
     */
    @Override
    public void validateObjectForSaving(ScsObjectDto object) throws RestException {
        log.info("Validating object {{}}", object.toString());
        
        // field validation
        validateFieldConstraints(object);
        
        // logical validation
        validateType(object.getType());
        validateGeoPosition(object.getGeoPosX(), object.getGeoPosY());
    }

    /**
     * Validates type, i.e.
     * checks if type is present in model types
     */
    @Override
    public void validateType(String type) throws RestException {
        log.debug("Validating object types");
        List<ScsTypeDto> types = typeRepository.findAllTypes();

        if (!types.stream().anyMatch(dto -> dto.getType().equals(type))) {
            log.error("Validating error for type {{}}", type);
            throw new RestException(RestExceptionEnum.ERR_000_004);
        }
    }

    /**
     * Validates geo position to be in the polygon,
     * specified for this shard
     */
    @Override
    public void validateGeoPosition(double geoPosX, double geoPosY) throws RestException {
        log.debug("Validating object positions");
        if (! geoValidationService.isPositionOk(geoPosX, geoPosY)) {
            log.error("Validating error for positions x={{}} y={{}}", geoPosX, geoPosY);
            throw new RestException(RestExceptionEnum.ERR_000_003);
        }
    }

    /**
     * Validates field constraints
     */
    @Override
    public void validateFieldConstraints(ScsObjectDto object) throws RestException {
        if (    object.getId() == null ||
                object.getType() == null ||
                object.getName() == null ||
                object.getGeoPosX() == null ||
                object.getGeoPosY() == null) {
            
            throw new RestException(RestExceptionEnum.ERR_000_005);
        }
    }
    
}
