package com.edu.netcracker.solution.scs.backend.data.service;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObject;
import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectMapper;
import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectRepository;
import com.edu.netcracker.solution.scs.backend.data.model.type.ScsTypeDto;
import com.edu.netcracker.solution.scs.backend.data.model.type.TypeRepository;
import com.edu.netcracker.solution.scs.backend.exception.RestException;
import com.edu.netcracker.solution.scs.backend.exception.RestExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class DataServiceImpl implements DataService {

    private final ScsObjectRepository scsObjectRepository;
    private final TypeRepository typeRepository;
    private final DataValidationService validator;
    private final ScsObjectMapper mapper;

    public DataServiceImpl(ScsObjectRepository scsObjectRepository,
                           TypeRepository typeRepository,
                           DataValidationService validator,
                           ScsObjectMapper mapper) {
        this.scsObjectRepository = scsObjectRepository;
        this.typeRepository = typeRepository;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Value("${scs.backend.shard-id}")
    private String shardId;

    @Override
    public List<ScsObjectDto> getObjectsInArea(double x1, double x2, double y1, double y2)
            throws RestException {

        log.debug("Fetching objects in area x1={{}} x2={{}} y1={{}} y2={{}}", x1, x2, y1, y2);
        validator.validateGeoPosition(x1, y1);
        validator.validateGeoPosition(x2, y2);

        double lowerX = Math.min(x1, x2);
        double upperX = Math.max(x1, x2);
        double lowerY = Math.min(y1, y2);
        double upperY = Math.max(y1, y2);

        Iterable<ScsObject> objectIterable = scsObjectRepository.findAllByGeoPosXIsBetweenAndGeoPosYIsBetween(lowerX, upperX, lowerY, upperY);

        return getObjectDtos(objectIterable);
    }

    @Override
    public List<ScsTypeDto> getAllTypes() {
        log.debug("Fetching types for shard with id {{}}", shardId);
        return typeRepository.findAllTypes();
    }

    @Override
    public List<ScsObjectDto> getAllObjects() {
        log.debug("Fetching objects for shard with id {{}}", shardId);
        Iterable<ScsObject> objectIterable = scsObjectRepository.findAll();

        return getObjectDtos(objectIterable);
    }

    private List<ScsObjectDto> getObjectDtos(Iterable<ScsObject> objectIterable) {
        List<ScsObject> entityList = new ArrayList<>();
        objectIterable.forEach(entityList::add);
        List<ScsObjectDto> dtoList = entityList.stream().map(mapper::toDto).collect(Collectors.toList());
        log.info("Object from area fetched, counted={{}}", dtoList.size());
        return dtoList;
    }

    @Override
    public ScsObjectDto getObject(String id) throws RestException {
        Optional<ScsObject> stored = scsObjectRepository.findById(id);
        if (stored.isPresent()) {
            ScsObjectDto object = mapper.toDto(stored.get());
            log.info("Object {{}} found by id {{}}", object, id);
            return object;
        } else {
            log.error("Object by id {{}} not found", id);
            throw new RestException(RestExceptionEnum.ERR_000_001);
        }
    }

    @Override
    public @NotNull String createObject(ScsObjectDto object) throws RestException {
        // id for ScsObject is SHARD-ID:UUID
        String id = String.format("%s:%s", shardId, UUID.randomUUID());
        object.setId(id);
        validator.validateObjectForSaving(object);
        ScsObject toStore = mapper.toEntity(object);

        ScsObject save = scsObjectRepository.save(toStore);
        log.info("Object created with id {{}}", save.getId());
        return save.getId();
    }

    @Override
    public void deleteObject(String id) {
        log.info("Deleting object with id {{}}", id);
        scsObjectRepository.deleteById(id);
    }

    /**
     * Partial update
     */
    @Override
    public void updateObjectFields(String id, ScsObjectDto object) throws RestException {
        Optional<ScsObject> stored = scsObjectRepository.findById(id);
        if (stored.isPresent()) {
            ScsObjectDto storedDto = mapper.toDto(stored.get());

            if (object.getType() == null) {
                object.setType(storedDto.getType());
            }
            if (object.getName() == null) {
                object.setName(storedDto.getName());
            }
            if (object.getDescription() == null) {
                object.setDescription(storedDto.getDescription());
            }
            if (object.getGeoPosX() == null) {
                object.setGeoPosX(storedDto.getGeoPosX());
            }
            if (object.getGeoPosY() == null) {
                object.setGeoPosY(storedDto.getGeoPosY());
            }

            if (object.getParameters() == null) {
                object.setParameters(storedDto.getParameters());
            } else {
                Map<String, Object> updateParams = object.getParameters();
                Map<String, Object> storedParams = storedDto.getParameters();

                storedParams.keySet().forEach(key -> {
                    if (! updateParams.containsKey(key)) {
                        updateParams.put(key, storedParams.get(key));
                    }
                });
            }

            // keeping same id
            object.setId(id);
            validator.validateObjectForSaving(object);
            ScsObject toStore = mapper.toEntity(object);
            scsObjectRepository.save(toStore);
            log.info("Object with id updated {{}}", id);
        }
        log.warn("Object with id NOT updated {{}}", id);
    }

    /**
     * Complete update
     */
    @Override
    public void updateObject(String id, ScsObjectDto object) throws RestException {
        Optional<ScsObject> stored = scsObjectRepository.findById(id);
        if (stored.isPresent()) {
            // keeping same id
            object.setId(id);
            validator.validateObjectForSaving(object);
            ScsObject toStore = mapper.toEntity(object);
            scsObjectRepository.save(toStore);
            log.info("Object with id updated {{}}", id);
        }
        log.warn("Object with id NOT update {{}}", id);
    }
}
