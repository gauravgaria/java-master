package com.eeip.javamaster.core.immutability;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImmutabilityFailureTest {

    @Test
    void mutableInputCanChangeObjectStateWhenNotDefensivelyCopied() {
        List<String> factors =
                new ArrayList<>(List.of("HIGH_CLAIM_AMOUNT"));

        MutableRiskProfile profile =
                new MutableRiskProfile(factors);

        factors.add("SUSPICIOUS_PROVIDER");

        assertEquals(2, profile.riskFactors().size());
    }

    private static final class MutableRiskProfile {

        private final List<String> riskFactors;

        private MutableRiskProfile(List<String> riskFactors) {
            this.riskFactors = riskFactors;
        }

        private List<String> riskFactors() {
            return riskFactors;
        }
    }
}