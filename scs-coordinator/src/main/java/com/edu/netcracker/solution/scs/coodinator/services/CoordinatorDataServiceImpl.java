package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CoordinatorDataServiceImpl implements CoordinatorDataService {

    @Autowired
    private CoordinatorClustersConfig clustersConfig;

    @Override
    public ScsObjectDTO getObject(String id, String url) {
        String shardId = id.split(":")[0];
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);

        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        ScsObjectDTO object = coordinatorRestTemplate.getForObject(url, ScsObjectDTO.class, headers);
        log.info("Object with id {{}} fetched from shard-id {{}}", object.getId(), shardId);
        return object;
    }

    @Override
    public String createObject(ScsObjectDTO object, String url) {
        String shardId = object.getShardId();
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);

        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String idJson = coordinatorRestTemplate.postForObject(url, object, String.class, headers);
        log.info("Object with {{}} created in shard-id {{}}", idJson, shardId);
        return idJson;
    }

    @Override
    public void deleteObject(String id, String url) {
        String shardId = id.split(":")[0];
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);

        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);

        coordinatorRestTemplate.delete(url);
        log.info("Object with id {{}} deleted from shard-id {{}}", id, shardId);
    }

    @Override
    public void updateObjectFields(String id, ScsObjectDTO object, String url) {
        String shardId = id.split(":")[0];
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);

        log.info("Update object field in cluster {{}}", cluster.toString());
        CoordinatorRestTemplate coordinatorRestTemplate =
                new CoordinatorRestTemplate(new HttpComponentsClientHttpRequestFactory(), cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        coordinatorRestTemplate.patchForObject(url, object, Void.class, headers);
        log.info("Object with id {{}} patched in shard-id {{}}", id, shardId);
    }

    @Override
    public void updateObject(String id, ScsObjectDTO object, String url) {
        String shardId = id.split(":")[0];
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);

        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        coordinatorRestTemplate.put(url, object, headers);
        log.info("Object with id {{}} updated in shard-id {{}}", id, shardId);
    }
}
