package com.eeip.javamaster.core.immutability;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable value representing a single insurance claim line.
 */
public record ClaimLine(
        String procedureCode,
        BigDecimal amount
) {

    public ClaimLine {
        Objects.requireNonNull(
                procedureCode,
                "procedureCode must not be null"
        );

        Objects.requireNonNull(
                amount,
                "amount must not be null"
        );

        if (amount.signum() < 0) {
            throw new IllegalArgumentException(
                    "amount must not be negative"
            );
        }
    }
}