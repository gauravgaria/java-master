package com.eeip.javamaster.core.topic5;

import java.util.Objects;

/** Type-safe container for the outcome of a processing step. */
public record ProcessingResult<T>(T value) {

    public ProcessingResult {
        Objects.requireNonNull(value, "value must not be null");
    }
}

