package com.eeip.javamaster.core.topic3.list;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.LinkedList;
import java.util.Objects;

/**
 * LinkedList is intentional here: a reviewer queue adds urgent work at the front
 * and removes completed work from the back. It is not a default replacement for ArrayList.
 */
public final class ClaimReviewTrail {
    private final LinkedList<Claim> claims = new LinkedList<>();

    public void addUrgent(Claim claim) { claims.addFirst(Objects.requireNonNull(claim, "claim must not be null")); }
    public void addRoutine(Claim claim) { claims.addLast(Objects.requireNonNull(claim, "claim must not be null")); }
    public Claim completeRoutine() { return claims.pollLast(); }
    public Claim nextReview() { return claims.peekFirst(); }
    public int size() { return claims.size(); }
}

