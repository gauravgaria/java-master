package com.eeip.javamaster.core.topic3.immutable;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.List;
import java.util.Objects;

/** Immutable boundary for a batch of claims; changes to the source list cannot leak in. */
public record ClaimSnapshot(List<Claim> claims) {
    public ClaimSnapshot {
        claims = List.copyOf(Objects.requireNonNull(claims, "claims must not be null"));
    }
}

