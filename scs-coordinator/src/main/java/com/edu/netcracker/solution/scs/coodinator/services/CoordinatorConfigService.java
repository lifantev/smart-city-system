package com.edu.netcracker.solution.scs.coodinator.services;

import com.edu.netcracker.solution.scs.coodinator.backendInfo.BackendInfoDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ClusterDataDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsObjectDTO;
import com.edu.netcracker.solution.scs.coodinator.backendInfo.ScsTypeDTO;

import java.util.List;

public interface CoordinatorConfigService {

    List<BackendInfoDTO> getInfo();

    List<ScsTypeDTO> getModel(String shardId);

    List<ScsObjectDTO> getObjects(String shardId);

    List<ClusterDataDTO> showObjects(double x1, double y1, double x2, double y2);

    boolean checkCrossing(BackendInfoDTO backendInfoDTO, double x1, double y1, double x2, double y2);

}
