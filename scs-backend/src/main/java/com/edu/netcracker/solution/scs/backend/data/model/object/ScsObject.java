package com.edu.netcracker.solution.scs.backend.data.model.object;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@NoArgsConstructor
@Document("scs-object")
public class ScsObject {

    @Id // db document field: _key
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;

    private String type;
    private String name;
    private String description;

    @Field("geo-pos-x")
    private double geoPosX;
    @Field("geo-pos-y")
    private double geoPosY;

    private Map<String, Object> parameters;

}
