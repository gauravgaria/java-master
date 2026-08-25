package com.eeip.javamaster.core.topic3.queue;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

/** PriorityQueue guarantees its head is highest priority; iteration is not sorted. */
public final class PriorityClaimQueue {
    private static final Comparator<Claim> BY_PRIORITY_THEN_NUMBER = Comparator
            .comparing(Claim::priority)
            .thenComparing(Claim::claimNumber);
    private final Queue<Claim> claims = new PriorityQueue<>(BY_PRIORITY_THEN_NUMBER);

    public boolean offer(Claim claim) { return claims.offer(Objects.requireNonNull(claim, "claim must not be null")); }
    public Claim poll() { return claims.poll(); }
    public Claim peek() { return claims.peek(); }
    public int size() { return claims.size(); }
}

