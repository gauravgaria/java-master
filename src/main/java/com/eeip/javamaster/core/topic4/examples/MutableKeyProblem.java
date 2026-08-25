package com.eeip.javamaster.core.topic4.examples;

import java.util.Objects;

/** Deliberately bad: changing a field used by hashCode after insertion loses normal lookup. */
public final class MutableKeyProblem {
    private String claimNumber;

    public MutableKeyProblem(String claimNumber) { this.claimNumber = requireText(claimNumber); }
    public void changeClaimNumber(String claimNumber) { this.claimNumber = requireText(claimNumber); }
    public String claimNumber() { return claimNumber; }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "claimNumber must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("claimNumber must not be blank");
        return value;
    }

    @Override public boolean equals(Object other) {
        return this == other || other instanceof MutableKeyProblem key && claimNumber.equals(key.claimNumber);
    }
    @Override public int hashCode() { return claimNumber.hashCode(); }
}
