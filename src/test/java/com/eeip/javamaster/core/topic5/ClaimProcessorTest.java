package com.eeip.javamaster.core.topic5;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClaimProcessorTest {

    private static final Claim CLAIM = new Claim(
            "CLM-2026-0005",
            new BigDecimal("2500.00")
    );

    private final ClaimProcessor processor = new ClaimProcessor();

    @Test
    void processingResultCanHoldClaimWithoutCast() {
        ProcessingResult<Claim> result = processor.acceptClaim(CLAIM);

        assertEquals(CLAIM, result.value());
    }

    @Test
    void processingResultCanHoldRiskAssessmentWithoutCast() {
        RiskAssessment assessment = new RiskAssessment(CLAIM.claimNumber(), "MEDIUM");
        ProcessingResult<RiskAssessment> result = processor.assessRisk(assessment);

        assertEquals(assessment, result.value());
    }

    @Test
    void genericMethodWorksWithClaimsAndRiskAssessments() {
        RiskAssessment assessment = new RiskAssessment(CLAIM.claimNumber(), "LOW");

        Optional<Claim> claim = processor.first(List.of(CLAIM));
        Optional<RiskAssessment> risk = processor.first(List.of(assessment));

        assertEquals(Optional.of(CLAIM), claim);
        assertEquals(Optional.of(assessment), risk);
    }

    @Test
    void boundedGenericMethodAcceptsNumberTypes() {
        assertTrue(new BigDecimal("12.50").compareTo(processor.amountForRisk(12.5)) == 0);
        assertTrue(new BigDecimal("7.0").compareTo(processor.amountForRisk(7)) == 0);
    }

    @Test
    void extendsProducerCanBeReadSafely() {
        List<Claim> claims = List.of(CLAIM);

        Optional<Claim> result = processor.firstClaim(claims);

        assertTrue(result.isPresent());
        assertEquals(CLAIM, result.orElseThrow());
    }

    @Test
    void superConsumerCanAcceptClaims() {
        List<Object> values = new ArrayList<>();

        processor.addClaim(values, CLAIM);

        assertEquals(List.of(CLAIM), values);
    }

    @Test
    void genericCollectionApiKeepsClaimValuesTypeSafe() {
        List<Claim> claims = new ArrayList<>();
        claims.add(CLAIM);

        Claim storedClaim = claims.getFirst();

        assertEquals(CLAIM, storedClaim);
    }
}



