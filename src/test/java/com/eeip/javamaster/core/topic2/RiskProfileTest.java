package com.eeip.javamaster.core.topic2;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiskProfileTest {

    private static final Instant CALCULATED_AT =
            Instant.parse("2026-08-16T10:00:00Z");

    @Test
    void riskFactorsMustBeDefensivelyCopied() {
        List<String> factors =
                new ArrayList<>(List.of(
                        "HIGH_CLAIM_AMOUNT"
                ));

        RiskProfile profile = new RiskProfile(
                "MEM-1001",
                RiskLevel.HIGH,
                factors,
                CALCULATED_AT
        );

        factors.add("MULTIPLE_RECENT_CLAIMS");

        assertEquals(
                List.of("HIGH_CLAIM_AMOUNT"),
                profile.riskFactors()
        );
    }

    @Test
    void riskFactorsMustNotBeModifiable() {
        RiskProfile profile = new RiskProfile(
                "MEM-1001",
                RiskLevel.HIGH,
                List.of("HIGH_CLAIM_AMOUNT"),
                CALCULATED_AT
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> profile.riskFactors().add(
                        "SUSPICIOUS_PROVIDER"
                )
        );
    }

    @Test
    void profileShouldExposeStableState() {
        RiskProfile profile = new RiskProfile(
                "MEM-1001",
                RiskLevel.HIGH,
                List.of("HIGH_CLAIM_AMOUNT"),
                CALCULATED_AT
        );

        assertEquals("MEM-1001", profile.memberId());
        assertEquals(RiskLevel.HIGH, profile.riskLevel());
        assertEquals(
                List.of("HIGH_CLAIM_AMOUNT"),
                profile.riskFactors()
        );
        assertEquals(CALCULATED_AT, profile.calculatedAt());
    }
}