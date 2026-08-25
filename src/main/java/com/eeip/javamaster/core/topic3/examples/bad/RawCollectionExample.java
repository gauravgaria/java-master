package com.eeip.javamaster.core.topic3.examples.bad;

import java.util.ArrayList;
import java.util.List;

/** Deliberately bad: raw collections remove compile-time type safety. */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class RawCollectionExample {
    public List claims() {
        List claims = new ArrayList();
        claims.add("not a Claim");
        return claims;
    }
}

