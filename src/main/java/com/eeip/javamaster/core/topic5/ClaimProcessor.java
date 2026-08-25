package com.eeip.javamaster.core.topic5;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Reusable, type-safe claim processing operations. */
public final class ClaimProcessor {

    public ProcessingResult<Claim> acceptClaim(Claim claim) {
        return new ProcessingResult<>(Objects.requireNonNull(claim, "claim must not be null"));
    }

    public ProcessingResult<RiskAssessment> assessRisk(RiskAssessment assessment) {
        return new ProcessingResult<>(Objects.requireNonNull(assessment, "assessment must not be null"));
    }

    /** Generic method: one implementation reads the first value from any typed collection. */
    public <T> Optional<T> first(List<T> values) {
        return values.stream().findFirst();
    }

    /** Producer Extends: a subtype list can safely provide Claim values for reading. */
    public Optional<Claim> firstClaim(List<? extends Claim> claims) {
        return claims.stream().<Claim>map(claim -> claim).findFirst();
    }

    /** Bounded type parameter: Number is required because the operation uses numeric conversion. */
    public <T extends Number> BigDecimal amountForRisk(T amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        return BigDecimal.valueOf(amount.doubleValue());
    }

    /** Consumer Super: this destination can consume a Claim even when its type is a supertype. */
    public void addClaim(List<? super Claim> destination, Claim claim) {
        destination.add(Objects.requireNonNull(claim, "claim must not be null"));
    }

    /*
     * List<Claim> is compile-time safe: unrelated values cannot be added.
     * Raw List, Object results, and casts would remove that safety.
     * No runtime check such as instanceof List<Claim> is possible after type erasure;
     * use instanceof List<?> when a runtime list check is genuinely needed.
     */
}



