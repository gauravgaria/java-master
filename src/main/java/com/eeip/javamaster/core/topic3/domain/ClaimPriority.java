package com.eeip.javamaster.core.topic3.domain;

public enum ClaimPriority {
    CRITICAL(0),
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private final int rank;

    ClaimPriority(int rank) {
        this.rank = rank;
    }

    public int rank() {
        return rank;
    }
}

