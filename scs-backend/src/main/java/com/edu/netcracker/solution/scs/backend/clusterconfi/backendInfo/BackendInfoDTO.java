package com.edu.netcracker.solution.scs.backend.clusterconfi.backendInfo;

import com.edu.netcracker.solution.scs.backend.clusterconfi.position.PositionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackendInfoDTO {

    private String name;
    @JsonProperty("shard-id")
    private String shardId;
    private List<PositionDTO> positions;

}
