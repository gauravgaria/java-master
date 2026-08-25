package com.eeip.javamaster.core.topic3.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** Immutable claim whose business identity is the stable claim number. */
public final class Claim implements Comparable<Claim> {
    private final String claimNumber;
    private final BigDecimal amount;
    private final LocalDate submittedOn;
    private final ClaimRiskLevel riskLevel;
    private final int riskScore;
    private final ClaimPriority priority;

    public Claim(String claimNumber, BigDecimal amount, LocalDate submittedOn,
                 ClaimRiskLevel riskLevel, int riskScore, ClaimPriority priority) {
        this.claimNumber = requireText(claimNumber, "claimNumber");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        if (amount.signum() < 0) throw new IllegalArgumentException("amount must not be negative");
        this.submittedOn = Objects.requireNonNull(submittedOn, "submittedOn must not be null");
        this.riskLevel = Objects.requireNonNull(riskLevel, "riskLevel must not be null");
        if (riskScore < 0) throw new IllegalArgumentException("riskScore must not be negative");
        this.riskScore = riskScore;
        this.priority = Objects.requireNonNull(priority, "priority must not be null");
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return value;
    }

    public String claimNumber() { return claimNumber; }
    public BigDecimal amount() { return amount; }
    public LocalDate submittedOn() { return submittedOn; }
    public ClaimRiskLevel riskLevel() { return riskLevel; }
    public int riskScore() { return riskScore; }
    public ClaimPriority priority() { return priority; }

    /** Natural order is stable business identity; use comparators for business queues/sorts. */
    @Override
    public int compareTo(Claim other) { return claimNumber.compareTo(other.claimNumber); }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Claim claim && claimNumber.equals(claim.claimNumber);
    }

    @Override
    public int hashCode() { return claimNumber.hashCode(); }
}

