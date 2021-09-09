package com.edu.netcracker.solution.scs.coodinator.backendInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypesObjectsDTO {
    List<ScsTypeDTO> types;
    List<ScsObjectDTO> objects;
}
