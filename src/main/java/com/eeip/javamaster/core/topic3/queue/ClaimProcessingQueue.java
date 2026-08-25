package com.eeip.javamaster.core.topic3.queue;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/** ArrayDeque gives a compact FIFO queue for bounded, single-owner work. */
public final class ClaimProcessingQueue {
    private final Deque<Claim> claims = new ArrayDeque<>();

    public boolean offer(Claim claim) { return claims.offer(Objects.requireNonNull(claim, "claim must not be null")); }
    public Claim poll() { return claims.poll(); }
    public Claim peek() { return claims.peek(); }
    public int size() { return claims.size(); }
}

