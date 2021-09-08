package com.edu.netcracker.solution.scs.backend.data.model.type;

import com.edu.netcracker.solution.scs.backend.exception.RestException;
import com.edu.netcracker.solution.scs.backend.exception.RestExceptionEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@Slf4j
public class TypeRepositoryImpl implements TypeRepository {

    @Value("classpath:${scs.backend.data.model}")
    private Resource resourceFile;

    // json list of types
    private String typesJson;

    @PostConstruct
    public void TypeRepositoryInit() throws RestException {
        log.info("Fetching model types");
        try {
            // initialize map with types from yaml
            ObjectMapper mapperYaml = new ObjectMapper(new YAMLFactory());
            Map<String, Object> typeMap = mapperYaml.readValue(resourceFile.getInputStream(),
                    new TypeReference<Map<String, Object>>() {});

            ObjectMapper mapperJson = new ObjectMapper();
            typesJson = mapperJson.writeValueAsString(typeMap);
        } catch (Exception e) {
            log.error("Model types fetching error from file {{}}", resourceFile.getFilename());
            throw new RestException(RestExceptionEnum.ERR_000_006);
        }
    }

    @Override
    public String findAllTypesJson() {
        return typesJson;
    }
}
