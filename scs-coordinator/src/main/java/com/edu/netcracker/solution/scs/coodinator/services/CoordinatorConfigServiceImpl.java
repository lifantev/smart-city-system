package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
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

    @Value("/api/v1/data/geo")
    private String URL_CLUSTERS_OBJECTS;

    private double MAXMOD = 90;

    @Autowired
    private List<List<Pair<String, Integer>>> clusters;

    @Autowired
    private CoordinatorClustersConfig clustersConfig;

    public List<BackendInfoDTO> getInfo() {
        List<BackendInfoDTO> list = new ArrayList<>();
        for (List<Pair<String, Integer>> cluster : clusters) {
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

    public List<ScsTypeDTO> getModel(String shardId) {
        List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(shardId);
        CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<ScsTypeDTO[]> response =
                coordinatorRestTemplate.getForEntity(URL_OBJECTS, ScsTypeDTO[].class, headers);
        ScsTypeDTO[] types = response.getBody();
        log.info("Model fetched for shard-id {{}}", shardId);
        return Arrays.asList(types);
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

    @Override
    public List<ClusterDataDTO> showObjects(double x1, double y1, double x2, double y2) throws URISyntaxException {
        List<ClusterDataDTO> clustersData = new ArrayList<>();

        URI newURL = UriComponentsBuilder.fromUri(new URI(URL_CLUSTERS_OBJECTS))
                .queryParam("x1", x1)
                .queryParam("x2", x2)
                .queryParam("y1", y1)
                .queryParam("y2", y2).build(1);

        List<BackendInfoDTO> backendInfoDTOS = getInfo();

        for (BackendInfoDTO backendInfoDTO : backendInfoDTOS) {
            log.debug("Check {{}}", backendInfoDTO.getName());
            if (checkCrossing(backendInfoDTO, x1, y1, x2, y2)) {
                List<Pair<String, Integer>> cluster = clustersConfig.clustersByShardIdMap().get(backendInfoDTO.getShardId());
                CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON);
                TypesObjectsDTO typesObjectsDTO = coordinatorRestTemplate.getForObject(newURL.toString(), TypesObjectsDTO.class, headers);
                log.debug("Cluster {{}} objects added", backendInfoDTO.getName());
                if (null != typesObjectsDTO) {
                    ClusterDataDTO data = new ClusterDataDTO(backendInfoDTO.getName(),
                            backendInfoDTO.getShardId(),
                            typesObjectsDTO.getTypes(),
                            typesObjectsDTO.getObjects()
                            );
                    clustersData.add(data);
                }
            }
        }

        if (clustersData.isEmpty())
            log.debug("No clusters in this area");

        return clustersData;
    }

    @Override
    public boolean checkCrossing(BackendInfoDTO backendInfoDTO, double x1, double y1, double x2, double y2) {
        PositionDTO LowerLeft = new PositionDTO(x1, y1);
        PositionDTO UpperRight = new PositionDTO(x2, y2);

        List<PositionDTO> positions = backendInfoDTO.getPositions();

        PositionDTO clusterLowerLeft = new PositionDTO(MAXMOD, MAXMOD);
        PositionDTO clusterUpperRight = new PositionDTO(-MAXMOD, -MAXMOD);

        for (PositionDTO pos : positions) {

            if (pos.getX() < clusterLowerLeft.getX())
                clusterLowerLeft.setX(pos.getX());
            if (pos.getY() < clusterLowerLeft.getY())
                clusterLowerLeft.setY(pos.getY());
            if (pos.getX() > clusterUpperRight.getX())
                clusterUpperRight.setX(pos.getX());
            if (pos.getY() > clusterUpperRight.getY())
                clusterUpperRight.setY(pos.getY());
        }

        if (clusterUpperRight.getX() < LowerLeft.getX())
            return false;
        if (clusterLowerLeft.getX() > UpperRight.getX())
            return false;
        if (clusterLowerLeft.getY() > UpperRight.getY())
            return false;
        if (clusterUpperRight.getY() < LowerLeft.getY())
            return false;

        return true;
    }
}
