package com.eeip.javamaster.core.topic3.sorting;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.Comparator;

/** Alternate business orderings belong here rather than being forced into Claim's natural order. */
public final class ClaimComparators {
    private ClaimComparators() { }

    public static Comparator<Claim> byAmountDescending() {
        return Comparator.comparing(Claim::amount).reversed().thenComparing(Claim::claimNumber);
    }

    public static Comparator<Claim> bySubmissionDate() {
        return Comparator.comparing(Claim::submittedOn).thenComparing(Claim::claimNumber);
    }

    public static Comparator<Claim> byRiskScoreDescending() {
        return Comparator.comparingInt(Claim::riskScore).reversed().thenComparing(Claim::claimNumber);
    }
}

