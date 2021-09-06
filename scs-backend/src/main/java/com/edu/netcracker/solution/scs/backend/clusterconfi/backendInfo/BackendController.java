package com.edu.netcracker.solution.scs.backend.clusterconfi.backendInfo;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectDto;
import com.edu.netcracker.solution.scs.backend.data.service.DataService;
import com.edu.netcracker.solution.scs.backend.exception.RestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/geo-sharding")
@RestController
public class BackendController {

    @Autowired
    private BackendInfoService backendInfoService;
    @Autowired
    private DataService dataService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public @ResponseBody
    BackendInfoDTO getInfo(){
        return backendInfoService.getBackendInfoDTO();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/model")
    public ResponseEntity<String> getModel() {
        String typesStr = dataService.getAllTypes();
        JSONObject typesJson = new JSONObject(typesStr);
        return new ResponseEntity<>(typesJson.get("types").toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/objects")
    public ResponseEntity<List<ScsObjectDto>> getObjects() throws RestException {
        return new ResponseEntity<>(dataService.getAllObjects(), HttpStatus.OK);
    }
}
