package com.eeip.javamaster.core.topic5;

import java.util.Objects;

/** Immutable risk outcome for a claim. */
public record RiskAssessment(String claimNumber, String riskLevel) {

    public RiskAssessment {
        Objects.requireNonNull(claimNumber, "claimNumber must not be null");
        Objects.requireNonNull(riskLevel, "riskLevel must not be null");
    }
}

