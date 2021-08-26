package com.edu.netcracker.solution.scs.coodinator.backendInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BackendInfoDTO {

    private String name;

    private List<PositionDTO> positions;
}
