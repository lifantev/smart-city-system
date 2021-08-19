package com.edu.netcracker.solution.scs.backend.data.model.object;

import com.arangodb.springframework.repository.ArangoRepository;

public interface ScsObjectRepository extends ArangoRepository<ScsObject, String> {
}
