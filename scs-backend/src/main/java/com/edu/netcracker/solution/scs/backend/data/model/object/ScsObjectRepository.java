package com.edu.netcracker.solution.scs.backend.data.model.object;

import com.arangodb.springframework.repository.ArangoRepository;

public interface ScsObjectRepository extends ArangoRepository<ScsObject, String> {

    /**
     * Find all by geoPosX is between and geoPosY is between
     */
    Iterable<ScsObject> findAllByGeoPosXIsBetweenAndGeoPosYIsBetween(
            double lowerX, double upperX, double lowerY, double upperY);
}
