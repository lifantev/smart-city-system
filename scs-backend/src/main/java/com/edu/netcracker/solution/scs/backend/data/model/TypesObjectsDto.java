package com.edu.netcracker.solution.scs.backend.data.model;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.data.model.type.ScsTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypesObjectsDto {
    List<ScsTypeDto> types;
    List<ScsObjectDto> objects;
}
