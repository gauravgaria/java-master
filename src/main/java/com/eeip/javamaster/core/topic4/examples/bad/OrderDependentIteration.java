package com.eeip.javamaster.core.topic4.examples.bad;

import java.util.HashMap;
import java.util.Map;

/** Deliberately bad: HashMap iteration order is not a business contract. */
public final class OrderDependentIteration {
    public Map<String, Integer> claims() {
        Map<String, Integer> claims = new HashMap<>();
        claims.put("first", 1);
        claims.put("second", 2);
        return claims;
    }
}

