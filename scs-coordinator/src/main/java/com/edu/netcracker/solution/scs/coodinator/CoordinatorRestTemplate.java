package com.edu.netcracker.solution.scs.coodinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

@Slf4j
@Component("coordinator-rest-template")
public class CoordinatorRestTemplate extends RestTemplate {

    private List<Pair<String, Integer>> servers;

    CoordinatorRestTemplate(List<Pair<String, Integer>> servers){
        this.servers = servers;
    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        //name like in super class
        Object var14;
        for (Pair<String, Integer> server : servers) {
            try {
                URI newUrl =  UriBuilder.fromUri(url).host(server.getLeft()).port(server.getRight()).build("foo", "bar");
                var14 = super.doExecute(newUrl, method, requestCallback, responseExtractor);
                if(null != var14) return (T) var14;
            } catch (Exception e) {
                log.warn("Unable to send request ", e);
            }
        }
        log.warn("Can't send request");
        return null;
    }
}
