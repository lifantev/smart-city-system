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
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

//import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

@Slf4j
public class CoordinatorRestTemplate extends RestTemplate {

    private List<Pair<String, Integer>> servers;

    CoordinatorRestTemplate(List<Pair<String, Integer>> servers){
        this.servers = servers;
    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        //name like in super class
        log.info("Start method doExecute with URI {{}} and method {{}}", url, method);
        Object var14;
        for (Pair<String, Integer> server : servers) {
            try {
//                URI newUrl =  UriBuilder.fromUri(url).host(server.getLeft()).port(server.getRight()).build("foo", "bar");
                URI newUrl =  new DefaultUriBuilderFactory()
                        .builder()
                        .host(server.getLeft())
                        .port(server.getRight())
                        .scheme("http")
                        .path(url.getPath()).build();
                log.info("New URI {{}} for server {{}}", newUrl, server);
                return super.doExecute(newUrl, method, requestCallback, responseExtractor);
//                if(null != var14) return (T) var14;
            } catch (Exception e) {
                log.warn("Unable to send request for server " + server, e);
            }
        }
        log.warn("Can't send request");
        return null;
    }
}
