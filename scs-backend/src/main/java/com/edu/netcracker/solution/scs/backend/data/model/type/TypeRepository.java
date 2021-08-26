package com.edu.netcracker.solution.scs.backend.data.model.type;

import java.util.Map;

public interface TypeRepository {

    String findAllTypeJson();

    Map<String, Object> findAllTypeMap();
}
