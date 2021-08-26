package com.edu.netcracker.solution.scs.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RestExceptionEnum {
    ERR_SYSTEM_INTERNAL("Unexpected system exception"),
    ERR_000_001("Object with id does not exist"),
    ERR_000_002("Object was not created"),
    ERR_000_003("Invalid geo position"),
    ERR_000_004("Invalid model type"),
    ERR_000_005("Invalid object fields"),
    ERR_000_006("Model types fetching error"),
    ;

    private String message;
}
