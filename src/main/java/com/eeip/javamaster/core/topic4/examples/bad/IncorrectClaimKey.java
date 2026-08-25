package com.eeip.javamaster.core.topic4.examples.bad;

/** Deliberately bad: equal keys return unrelated hash codes. */
public final class IncorrectClaimKey {
    private final String claimNumber;

    public IncorrectClaimKey(String claimNumber) { this.claimNumber = claimNumber; }
    @Override public boolean equals(Object other) {
        return other instanceof IncorrectClaimKey key && claimNumber.equals(key.claimNumber);
    }
    @Override public int hashCode() { return System.identityHashCode(this); }
}

