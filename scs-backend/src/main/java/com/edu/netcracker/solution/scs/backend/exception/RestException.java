package com.edu.netcracker.solution.scs.backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * String representation of error is JSON with "date", "code" and "message" fields
 */
public class RestException extends Exception {

    private final RestExceptionEnum error;
    private final Date date = new Date();

    public RestException(Throwable throwable) {
        super(throwable);
        this.error = RestExceptionEnum.ERR_SYSTEM_INTERNAL;
    }

    public RestException(RestExceptionEnum error) {
        this.error = error;
    }

    @Override
    @SneakyThrows
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> restResult = new TreeMap<>();

        restResult.put("date", date.toString());
        restResult.put("code", error.name());
        restResult.put("message", error.getMessage());

        return objectMapper.writeValueAsString(restResult);
    }
}
