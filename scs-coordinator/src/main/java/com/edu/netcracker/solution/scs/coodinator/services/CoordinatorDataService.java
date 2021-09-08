package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;

public interface CoordinatorDataService {

    ScsObjectDTO getObject(String id, String url);

    String createObject(ScsObjectDTO object, String url);

    void deleteObject(String id, String url);

    void updateObjectFields(String id, ScsObjectDTO object, String url);

    void updateObject(String id, ScsObjectDTO object, String url);
}
