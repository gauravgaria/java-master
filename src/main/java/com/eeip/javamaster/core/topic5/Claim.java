package com.eeip.javamaster.core.topic5;

import java.math.BigDecimal;
import java.util.Objects;

/** Immutable claim data used by the generics example. */
public record Claim(String claimNumber, BigDecimal claimAmount) {

    public Claim {
        Objects.requireNonNull(claimNumber, "claimNumber must not be null");
        Objects.requireNonNull(claimAmount, "claimAmount must not be null");
    }
}

