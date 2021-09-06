package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("coordinator-config-service")
@Slf4j
public class CoordinatorConfigServiceImpl implements CoordinatorConfigService {

    @Value("/api/v1/geo-sharding/config")
    private String URL_CONFIG;

    @Value("/api/v1/geo-sharding/model")
    private String URL_MODEL;

    @Value("/api/v1/geo-sharding/objects")
    private String URL_OBJECTS;

    @Autowired
    private List<List<Pair<String, Integer>>> clusters;

    @Autowired
    private CoordinatorClustersConfig clustersConfig;

    public List<BackendInfoDTO> getInfo(){
        List<BackendInfoDTO> list = new ArrayList<>();
        for (List<Pair<String, Integer>> cluster: clusters) {
            CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);
            BackendInfoDTO backendInfoDTO = coordinatorRestTemplate.getForObject(URL_CONFIG, BackendInfoDTO.class, headers);
            log.debug("Comes from request {{}}", backendInfoDTO);
            list.add(backendInfoDTO);
        }
        return list;
    }

    public String getModel(String shardId) {
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);
        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String modelJson = coordinatorRestTemplate.getForObject(URL_MODEL, String.class, headers);
        log.info("Model fetched for shard-id {{}}", shardId);
        return modelJson;
    }

    public List<ScsObjectDTO> getObjects(String shardId) {
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);
        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<ScsObjectDTO[]> response =
                coordinatorRestTemplate.getForEntity(URL_OBJECTS, ScsObjectDTO[].class, headers);
        ScsObjectDTO[] objects = response.getBody();
        log.info("Objects fetched for shard-id {{}}", shardId);
        return Arrays.asList(objects);
    }
}
