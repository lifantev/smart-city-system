package com.edu.netcracker.solution.scs.coodinator;

import com.edu.netcracker.solution.scs.backend.BackendInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("coordinator-config-service")
public class CoordinatorConfigService {

    @Value("http://localhost:27011/api/v1/geo-sharding/config")
    private static String URL;

    @Autowired
    private List<List<Pair<String, Integer>>> clusters;

    public List<BackendInfo> getInfo(){
        List<BackendInfo> list = new ArrayList<>();
        for (List<Pair<String, Integer>> cluster: clusters) {
            CoordinatorRestTemplate coordinatorRestTemplate = new CoordinatorRestTemplate(cluster);
            list.add(coordinatorRestTemplate.getForObject(URL, BackendInfo.class));
        }
        return list;
    }
}
