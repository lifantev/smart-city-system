package com.edu.netcracker.solution.scs.coodinator.backendInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusterDataDTO {
    String cluster;
    @JsonProperty("shard-id")
    String shardId;
    List<ScsTypeDTO> types;
    List<ScsObjectDTO> objects;
}
