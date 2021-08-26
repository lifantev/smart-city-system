package com.edu.netcracker.solution.scs.backend.clusterconfi.backendInfo;

import com.edu.netcracker.solution.scs.backend.clusterconfi.position.PositionDTO;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackendInfoDTO {

    private String name;

    private List<PositionDTO> positions;

}
