package com.eeip.javamaster.core.topic4.domain;

import java.math.BigDecimal;
import java.util.Objects;

/** Minimal immutable EIIP claim used as a HashMap value. */
public record Claim(String claimNumber, String memberId, BigDecimal claimAmount,
                    int riskScore, ClaimStatus status) {
    public Claim {
        requireText(claimNumber, "claimNumber");
        requireText(memberId, "memberId");
        Objects.requireNonNull(claimAmount, "claimAmount must not be null");
        if (claimAmount.signum() < 0) throw new IllegalArgumentException("claimAmount must not be negative");
        if (riskScore < 0) throw new IllegalArgumentException("riskScore must not be negative");
        Objects.requireNonNull(status, "status must not be null");
    }

    private static void requireText(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Claim claim && claimNumber.equals(claim.claimNumber);
    }

    @Override
    public int hashCode() {
        return claimNumber.hashCode();
    }
}

