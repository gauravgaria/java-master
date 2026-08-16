package com.eeip.javamaster.core.immutability;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ClaimApprovedEventTest {

    @Test
    void equivalentEventsShouldBeEqual() {
        Instant occurredAt =
                Instant.parse("2026-08-16T10:00:00Z");

        ClaimApprovedEvent first =
                new ClaimApprovedEvent(
                        "EVT-001",
                        "CLM-2026-0001",
                        new BigDecimal("25000.00"),
                        occurredAt
                );

        ClaimApprovedEvent second =
                new ClaimApprovedEvent(
                        "EVT-001",
                        "CLM-2026-0001",
                        new BigDecimal("25000.00"),
                        occurredAt
                );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void negativeApprovedAmountMustBeRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ClaimApprovedEvent(
                        "EVT-001",
                        "CLM-2026-0001",
                        new BigDecimal("-1"),
                        Instant.now()
                )
        );
    }
}