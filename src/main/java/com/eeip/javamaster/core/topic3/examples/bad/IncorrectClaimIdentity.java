package com.eeip.javamaster.core.topic3.examples.bad;

/** Deliberately bad: equal business identities have unequal hash codes. */
public final class IncorrectClaimIdentity {
    private final String claimNumber;
    public IncorrectClaimIdentity(String claimNumber) { this.claimNumber = claimNumber; }
    @Override public boolean equals(Object other) {
        return other instanceof IncorrectClaimIdentity that && claimNumber.equals(that.claimNumber);
    }
    @Override public int hashCode() { return System.identityHashCode(this); }
}

