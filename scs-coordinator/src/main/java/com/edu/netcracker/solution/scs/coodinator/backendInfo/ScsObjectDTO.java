package com.edu.netcracker.solution.scs.coodinator.backendInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScsObjectDTO {
    private String id;
    @JsonProperty("shard-id")
    private String shardId;
    private String type;
    private String name;
    private String description;
    @JsonProperty("geo-pos-x")
    private Double geoPosX;
    @JsonProperty("geo-pos-y")
    private Double geoPosY;
    private Map<String, Object> parameters;
}
