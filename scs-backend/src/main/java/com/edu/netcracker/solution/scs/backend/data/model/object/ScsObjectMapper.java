package com.edu.netcracker.solution.scs.backend.data.model.object;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ScsObjectMapper {

    private final ModelMapper modelMapper;

    public ScsObjectDto toDto(ScsObject entity) {
        log.debug("Converting entity {{}} to dto", entity.toString());
        return modelMapper.map(entity, ScsObjectDto.class);
    }

    public ScsObject toEntity(ScsObjectDto dto) {
        log.debug("Converting dto {{}} to entity", dto.toString());
        return modelMapper.map(dto, ScsObject.class);
    }
}
