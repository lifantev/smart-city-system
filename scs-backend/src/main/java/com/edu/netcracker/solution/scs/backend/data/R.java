package com.edu.netcracker.solution.scs.backend.data;

import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObject;
import com.edu.netcracker.solution.scs.backend.data.model.object.ScsObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class R implements CommandLineRunner {

    @Autowired
    private ScsObjectRepository repository;

    @Override
    public void run(String... args) throws Exception {

        ScsObject scsObject = new ScsObject();
        scsObject.setName("Test");
        scsObject.setDescription("Test Object");
        scsObject.setParameters(new HashMap<String, Object>(){{
            put("param 1", "value 1");
            put("param 2", 3);
        }});
        scsObject.setId("my_id");

        scsObject.setGeoPosX(1254.51);
        scsObject.setGeoPosY(1254.51);
        
        repository.save(scsObject);
    }
}
