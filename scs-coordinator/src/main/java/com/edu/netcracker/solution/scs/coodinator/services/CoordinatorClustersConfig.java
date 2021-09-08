package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class CoordinatorClustersConfig {

    @Value("/api/v1/geo-sharding/config")
    private String URL_CONFIG;

    @Autowired
    private List<List<Pair<String, Integer>>> clusters;

    private Map<String, List<Pair<String, Integer>>> clustersByShardIdMap;

    public Map<String, List<Pair<String, Integer>>> clustersByShardIdMap() {
        if (clustersByShardIdMap == null) {
            clustersByShardIdMap = new HashMap<>();
            for (List<Pair<String, Integer>> cluster : clusters) {
                CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON);
                BackendInfoDTO backendInfoDTO = coordinatorRestTemplate.getForObject(URL_CONFIG, BackendInfoDTO.class, headers);
                clustersByShardIdMap.put(backendInfoDTO.getShardId(), cluster);
            }
        }
        return clustersByShardIdMap;
    }
}
