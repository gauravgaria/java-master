package com.eeip.javamaster.core.topic2;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Immutable risk assessment result suitable for safe sharing and caching.
 */
public final class RiskProfile {

    private final String memberId;
    private final RiskLevel riskLevel;
    private final List<String> riskFactors;
    private final Instant calculatedAt;

    public RiskProfile(
            String memberId,
            RiskLevel riskLevel,
            List<String> riskFactors,
            Instant calculatedAt
    ) {
        this.memberId = Objects.requireNonNull(
                memberId,
                "memberId must not be null"
        );

        this.riskLevel = Objects.requireNonNull(
                riskLevel,
                "riskLevel must not be null"
        );

        this.riskFactors = List.copyOf(
                Objects.requireNonNull(
                        riskFactors,
                        "riskFactors must not be null"
                )
        );

        this.calculatedAt = Objects.requireNonNull(
                calculatedAt,
                "calculatedAt must not be null"
        );
    }

    public String memberId() {
        return memberId;
    }

    public RiskLevel riskLevel() {
        return riskLevel;
    }

    public List<String> riskFactors() {
        return riskFactors;
    }

    public Instant calculatedAt() {
        return calculatedAt;
    }
}