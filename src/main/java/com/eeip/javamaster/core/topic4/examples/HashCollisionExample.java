package com.eeip.javamaster.core.topic4.examples;

import java.util.Objects;

/** A behavioral collision key: same hash narrows to one bucket, equals keeps keys distinct. */
public record HashCollisionExample(String label) {
    public HashCollisionExample {
        Objects.requireNonNull(label, "label must not be null");
    }

    @Override
    public int hashCode() { return 42; }
}

