package com.eeip.javamaster.core.immutability;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Immutable event representing a claim approval.
 */
public record ClaimApprovedEvent(
        String eventId,
        String claimNumber,
        BigDecimal approvedAmount,
        Instant occurredAt
) {

    public ClaimApprovedEvent {
        Objects.requireNonNull(
                eventId,
                "eventId must not be null"
        );

        Objects.requireNonNull(
                claimNumber,
                "claimNumber must not be null"
        );

        Objects.requireNonNull(
                approvedAmount,
                "approvedAmount must not be null"
        );

        Objects.requireNonNull(
                occurredAt,
                "occurredAt must not be null"
        );

        if (approvedAmount.signum() < 0) {
            throw new IllegalArgumentException(
                    "approvedAmount must not be negative"
            );
        }
    }
}