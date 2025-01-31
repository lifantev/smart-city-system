package com.edu.netcracker.solution.scs.backend.data.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScsObjectDto {
    private String id;
    private String type;
    private String name;
    private String description;
    @JsonProperty("geo-pos-x")
    private Double geoPosX;
    @JsonProperty("geo-pos-y")
    private Double geoPosY;
    private Map<String, Object> parameters;
}
