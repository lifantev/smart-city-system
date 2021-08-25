package com.edu.netcracker.solution.scs.coodinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("coordinator-config-service")
@Slf4j
public class CoordinatorConfigService {

    @Value("/api/v1/geo-sharding/config")
    private String URL;

    @Autowired
    private List<List<Pair<String, Integer>>> clusters;

    public List<BackendInfoDTO> getInfo(){
        log.warn("method start");
        List<BackendInfoDTO> list = new ArrayList<>();
        for (List<Pair<String, Integer>> cluster: clusters) {
            CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);
            BackendInfoDTO backendInfoDTO = coordinatorRestTemplate.getForObject(URL, BackendInfoDTO.class, headers);
            log.warn("comes from request {{}}", backendInfoDTO);
            list.add(backendInfoDTO);
        }
        return list;
    }
}
