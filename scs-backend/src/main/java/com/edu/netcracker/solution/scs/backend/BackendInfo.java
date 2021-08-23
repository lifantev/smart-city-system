package com.edu.netcracker.solution.scs.backend;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component("backend-info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackendInfo {
    @Value("${SCS_CLUSTER_NAME}")
    private String name;

    private List<Position> positions;

    public BackendInfo(PositionConfiguration positionConfiguration){
        positions = positionConfiguration.getPos();
    }
}
