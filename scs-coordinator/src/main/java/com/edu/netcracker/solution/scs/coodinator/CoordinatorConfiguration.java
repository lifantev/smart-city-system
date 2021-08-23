package com.edu.netcracker.solution.scs.coodinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class CoordinatorConfiguration {

    @Value("${scs.coordinator.backends}")
    private String clusters;

    @Bean("backend-clusters")
    public List<List<Pair<String, Integer>>> backendClusters(){
        String [] list1 = clusters.split(" ");
        List<List<String>> backends = new ArrayList<>();
        for (int i = 0; i < list1.length; i++) {
            String[] helpArray = list1[i].split("\\|");
            backends.add(Arrays.asList(helpArray));
        }
        List<List<Pair<String, Integer>>> servers = new ArrayList<>();
        List<Pair<String, Integer>> helpList = new ArrayList<>();
        for (int i = 0; i < backends.size(); i++) {
            for (int j = 0; j < backends.get(i).size(); j++) {
                String[] list = backends.get(i).get(j).split(":");
                if(list.length != 2) log.warn("Value of {} is not available", clusters);
                helpList.add(Pair.of(list[0], Integer.parseInt(list[1])));
            }
            List<Pair<String, Integer>> valueOfHelpList = new ArrayList<>();
            valueOfHelpList.addAll(helpList);
            servers.add(valueOfHelpList);
            helpList.clear();
        }
        return servers;
    }
}
