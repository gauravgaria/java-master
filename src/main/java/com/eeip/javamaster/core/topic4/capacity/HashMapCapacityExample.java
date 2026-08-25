package com.eeip.javamaster.core.topic4.capacity;

import com.eeip.javamaster.core.topic4.domain.Claim;

import java.util.HashMap;
import java.util.Map;

/** Documents capacity choices without inspecting private HashMap internals. */
public final class HashMapCapacityExample {
    private HashMapCapacityExample() { }

    public static Map<String, Claim> defaultCapacity() { return new HashMap<>(); }
    public static Map<String, Claim> preSizedFor(int expectedEntries) {
        if (expectedEntries < 0) throw new IllegalArgumentException("expectedEntries must not be negative");
        return new HashMap<>(expectedEntries);
    }

    /** Approximate capacity planning; the JDK may round capacity and resize implementation details vary. */
    public static int capacityFor(int expectedEntries, float loadFactor) {
        if (expectedEntries < 0) throw new IllegalArgumentException("expectedEntries must not be negative");
        if (!(loadFactor > 0.0f && loadFactor < 1.0f)) throw new IllegalArgumentException("loadFactor must be between 0 and 1");
        return (int) Math.ceil(expectedEntries / loadFactor);
    }
}

