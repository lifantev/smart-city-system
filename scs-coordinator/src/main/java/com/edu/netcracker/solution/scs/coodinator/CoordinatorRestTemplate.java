package com.edu.netcracker.solution.scs.coodinator;

import lombok.SneakyThrows;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
public class CoordinatorRestTemplate extends RestTemplate {

    private List<Pair<String, Integer>> servers;

    CoordinatorRestTemplate(List<Pair<String, Integer>> servers){
        this.servers = servers;
    }

    @SneakyThrows
    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        log.info("Start method doExecute with URI {{}} and method {{}}", url, method);
        for (Pair<String, Integer> server : servers) {
            try {

                URI newUrl = UriComponentsBuilder.newInstance()
                        .host(server.getLeft())
                        .port(server.getRight())
                        .scheme("http")
                        .path(url.getPath()).build(1);

                log.info("New URI {{}} for server {{}}", newUrl, server);
                return super.doExecute(newUrl, method, requestCallback, responseExtractor);

            } catch (Exception e) {
                log.warn("Unable to send request for server " + server, e);
            }
        }
        throw new IOException("Cant reach any server in cluster" + servers);
    }
}
