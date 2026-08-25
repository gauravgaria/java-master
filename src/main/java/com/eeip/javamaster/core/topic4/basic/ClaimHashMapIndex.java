package com.eeip.javamaster.core.topic4.basic;

import com.eeip.javamaster.core.topic4.domain.Claim;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Local, bounded claim-number lookup; HashMap gives expected average O(1) operations. */
public final class ClaimHashMapIndex {
    private final Map<String, Claim> claimsByNumber = new HashMap<>();

    public Claim put(Claim claim) {
        Objects.requireNonNull(claim, "claim must not be null");
        return claimsByNumber.put(claim.claimNumber(), claim);
    }

    public Claim get(String claimNumber) { return claimsByNumber.get(claimNumber); }
    public boolean containsKey(String claimNumber) { return claimsByNumber.containsKey(claimNumber); }
    public Claim remove(String claimNumber) { return claimsByNumber.remove(claimNumber); }
    public int size() { return claimsByNumber.size(); }
}

