package com.eeip.javamaster.core.topic2;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable snapshot of a claim at a specific point in time.
 *
 * Collection boundaries are protected through defensive copying.
 */
public final class ClaimSnapshot {

    private final String claimNumber;
    private final List<ClaimLine> claimLines;
    private final List<String> diagnosisCodes;
    private final Map<String, String> metadata;
    private final Instant capturedAt;

    public ClaimSnapshot(
            String claimNumber,
            List<ClaimLine> claimLines,
            List<String> diagnosisCodes,
            Map<String, String> metadata,
            Instant capturedAt
    ) {
        this.claimNumber = Objects.requireNonNull(
                claimNumber,
                "claimNumber must not be null"
        );

        this.claimLines = List.copyOf(
                Objects.requireNonNull(
                        claimLines,
                        "claimLines must not be null"
                )
        );

        this.diagnosisCodes = List.copyOf(
                Objects.requireNonNull(
                        diagnosisCodes,
                        "diagnosisCodes must not be null"
                )
        );

        this.metadata = Map.copyOf(
                Objects.requireNonNull(
                        metadata,
                        "metadata must not be null"
                )
        );

        this.capturedAt = Objects.requireNonNull(
                capturedAt,
                "capturedAt must not be null"
        );
    }

    public String claimNumber() {
        return claimNumber;
    }

    public List<ClaimLine> claimLines() {
        return claimLines;
    }

    public List<String> diagnosisCodes() {
        return diagnosisCodes;
    }

    public Map<String, String> metadata() {
        return metadata;
    }

    public Instant capturedAt() {
        return capturedAt;
    }
}