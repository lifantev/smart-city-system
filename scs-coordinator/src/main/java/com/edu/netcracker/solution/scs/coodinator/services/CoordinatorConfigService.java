package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;

import java.util.List;

public interface CoordinatorConfigService {

    List<BackendInfoDTO> getInfo();

    String getModel(String shardId);

    List<ScsObjectDTO> getObjects(String shardId);
}
