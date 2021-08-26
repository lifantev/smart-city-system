package com.edu.netcracker.solution.scs.backend.data.model.object;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Field;
import com.arangodb.springframework.annotation.HashIndexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@Document("scs_object")
public class ScsObject {

    @Id // db document field: _key
    @NotNull
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;

    @NotNull
    private String type;

    @NotNull
    private String name;

    private String description;

    @Field("geo_pos_x")
    @HashIndexed
    @NotNull
    private Double geoPosX;

    @Field("geo_pos_y")
    @HashIndexed
    @NotNull
    private Double geoPosY;

    private Map<String, Object> parameters;
}
