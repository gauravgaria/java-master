package com.eeip.javamaster.core.topic4.identity;

import java.util.Objects;

/** Immutable HashMap key. Claim number is the pre-persistence business identity. */
public record ClaimKey(String claimNumber) {
    public ClaimKey {
        Objects.requireNonNull(claimNumber, "claimNumber must not be null");
        if (claimNumber.isBlank()) throw new IllegalArgumentException("claimNumber must not be blank");
    }
}

