package com.eeip.javamaster.core.topic3.map;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.LinkedHashMap;
import java.util.Map;

/** LinkedHashMap makes audit/report output deterministic by retaining insertion order. */
public final class OrderedClaimIndex {
    private final Map<String, Claim> claims = new LinkedHashMap<>();

    public void put(Claim claim) { claims.put(claim.claimNumber(), claim); }
    public Claim get(String claimNumber) { return claims.get(claimNumber); }
    public Iterable<Claim> inInsertionOrder() { return claims.values(); }
}

