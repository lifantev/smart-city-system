package com.edu.netcracker.solution.scs.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class PositionConfiguration {

    @Value("${SCS_CLUSTER_POSITIONS}")
    private String position;

    @Bean("backend-positions")
    public List<PositionDTO> getPos() throws Exception {
        String [] positions = position.split(" ");
        List<PositionDTO> positionsList = new ArrayList<>();
        for (int i = 0; i < positions.length; i++) {
            String[] helpPos = positions[i].split(":");
            if(helpPos.length != 2) {
                throw new Exception("Wring format for SCS_CLUSTER_POSITIONS environment variable, unable to parse");
            }
            positionsList.add(new PositionDTO(Double.parseDouble(helpPos[0]), Double.parseDouble(helpPos[1])));
        }
        return positionsList;
    }
}
