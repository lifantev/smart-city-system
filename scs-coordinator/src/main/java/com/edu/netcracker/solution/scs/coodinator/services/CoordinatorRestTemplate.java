package com.edu.netcracker.solution.scs.coodinator.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
public class CoordinatorRestTemplate extends RestTemplate {

    private List<Pair<String, Integer>> servers;

    CoordinatorRestTemplate(ClientHttpRequestFactory requestFactory, List<Pair<String, Integer>> servers) {
        super();
        this.setRequestFactory(requestFactory);
        this.servers = servers;
    }

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
                        .path(url.getRawPath()).build(1);

                if (null != url.getQuery())
                    newUrl = UriComponentsBuilder.fromUri(newUrl)
                            .query(url.getQuery()).build(1);

                log.info("New URI {{}} for server {{}}", newUrl, server);
                return super.doExecute(newUrl, method, requestCallback, responseExtractor);

            } catch (Exception e) {
                log.warn("Unable to send request for server " + server, e);
            }
        }
        throw new IOException("Cant reach any server in cluster" + servers);
    }
}
