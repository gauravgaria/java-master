package com.eeip.javamaster.core.topic4.examples.bad;

import com.eeip.javamaster.core.topic4.domain.Claim;

import java.util.HashMap;
import java.util.Map;

/** Deliberately bad design: no eviction, bound, or ownership policy exists. */
public final class UnboundedClaimMap {
    private final Map<String, Claim> allClaimsEverSeen = new HashMap<>();

    public void retainForever(Claim claim) { allClaimsEverSeen.put(claim.claimNumber(), claim); }
    public int size() { return allClaimsEverSeen.size(); }
}

