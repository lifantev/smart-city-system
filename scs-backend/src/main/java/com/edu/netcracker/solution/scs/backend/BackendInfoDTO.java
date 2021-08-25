package com.edu.netcracker.solution.scs.backend;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BackendInfoDTO {

    private String name;

    private List<PositionDTO> positions;

}
