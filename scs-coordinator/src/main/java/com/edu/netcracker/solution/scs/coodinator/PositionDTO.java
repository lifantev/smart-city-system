package com.edu.netcracker.solution.scs.coodinator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionDTO {
    private double x;
    private double y;
}
