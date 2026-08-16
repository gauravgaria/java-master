package com.eeip.javamaster.core.equals;

import java.util.Objects;

/**
 * Immutable business identity for an insurance claim.
 *
 * Claim number is assumed to be globally unique within the claim domain
 * and immutable once assigned.
 */
public final class ClaimKey {

    private final String claimNumber;

    public ClaimKey(String claimNumber) {
        this.claimNumber = Objects.requireNonNull(
                claimNumber,
                "claimNumber must not be null"
        );
    }

    public String claimNumber() {
        return claimNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ClaimKey that)) {
            return false;
        }

        return claimNumber.equals(that.claimNumber);
    }

    @Override
    public int hashCode() {
        return claimNumber.hashCode();
    }
}