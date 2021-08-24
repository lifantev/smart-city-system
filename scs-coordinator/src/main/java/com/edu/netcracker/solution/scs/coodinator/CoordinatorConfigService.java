package com.edu.netcracker.solution.scs.coodinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("coordinator-config-service")
@Slf4j
public class CoordinatorConfigService {

    @Value("http://localhost:27011/api/v1/geo-sharding/config")
    private static String URL;

    @Autowired
    private List<List<Pair<String, Integer>>> clusters;

    public List<BackendInfoDTO> getInfo(){
        log.warn("method start");
        List<BackendInfoDTO> list = new ArrayList<>();
        for (List<Pair<String, Integer>> cluster: clusters) {
            CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
            BackendInfoDTO backendInfoDTO = coordinatorRestTemplate.getForObject(URL, BackendInfoDTO.class);
            log.warn("comes from request {{}}", backendInfoDTO);
            list.add(backendInfoDTO);
        }
        return list;
    }
}
