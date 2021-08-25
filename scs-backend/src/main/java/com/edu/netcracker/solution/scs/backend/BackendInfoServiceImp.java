package com.edu.netcracker.solution.scs.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("backend-info-service")
@Slf4j
public class BackendInfoServiceImp {

    @Value("${SCS_CLUSTER_NAME}")
    private String name;

    @Autowired
    @Qualifier("backend-positions")
    private List<PositionDTO> positions;


    public BackendInfoDTO getBackendInfoDTO(){
        log.debug("Start to get backend info");
        BackendInfoDTO backendInfoDTO = new BackendInfoDTO();
        backendInfoDTO.setName(name);
        backendInfoDTO.setPositions(positions);
        log.debug("Backend info {{}}", backendInfoDTO);
        return backendInfoDTO;
    }

}
