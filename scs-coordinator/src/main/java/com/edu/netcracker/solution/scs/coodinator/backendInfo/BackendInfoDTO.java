package com.edu.netcracker.solution.scs.coodinator.backendInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BackendInfoDTO {

    private String name;
    @JsonProperty("shard-id")
    private String shardId;
    private List<PositionDTO> positions;
}
