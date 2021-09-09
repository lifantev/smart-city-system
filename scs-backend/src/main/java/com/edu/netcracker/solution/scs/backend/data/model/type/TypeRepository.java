package com.edu.netcracker.solution.scs.backend.data.model.type;

import java.util.List;

public interface TypeRepository {

    List<ScsTypeDto> findAllTypes();
}
