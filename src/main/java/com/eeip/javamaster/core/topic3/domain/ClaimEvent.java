package com.eeip.javamaster.core.topic3.domain;

import java.time.Instant;
import java.util.Objects;

public record ClaimEvent(ClaimStatus status, Instant occurredAt) {
    public ClaimEvent {
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}

