package com.eeip.javamaster.core.topic3.map;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/** HashMap is the default claim-number index: expected O(1) lookup with no ordering promise. */
public final class ClaimIndex {
    private final Map<String, Claim> claims = new HashMap<>();

    public void put(Claim claim) { claims.put(Objects.requireNonNull(claim).claimNumber(), claim); }
    public Claim get(String claimNumber) { return claims.get(claimNumber); }
    public boolean containsKey(String claimNumber) { return claims.containsKey(claimNumber); }
    public Claim remove(String claimNumber) { return claims.remove(claimNumber); }
    public Claim getOrCreate(String claimNumber, Supplier<Claim> creator) {
        Objects.requireNonNull(creator, "creator must not be null");
        return claims.computeIfAbsent(Objects.requireNonNull(claimNumber), ignored -> creator.get());
    }
    public int size() { return claims.size(); }
}
