package com.edu.netcracker.solution.scs.backend.data.model.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScsTypeDto {
    String type;
    @JsonProperty("displayed-name")
    String displayedName;
    String icon;
    List<Object> attributes;
    String actions;
}
