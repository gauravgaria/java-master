package com.eeip.javamaster.core.topic3.map;

import com.eeip.javamaster.core.topic3.domain.Claim;

import java.util.Map;
import java.util.TreeMap;

/** TreeMap supports ordered claim-number navigation, typically O(log n). */
public final class SortedClaimIndex {
    private final TreeMap<String, Claim> claims = new TreeMap<>();

    public void put(Claim claim) { claims.put(claim.claimNumber(), claim); }
    public String firstKey() { return claims.firstKey(); }
    public String lastKey() { return claims.lastKey(); }
    public String floorKey(String key) { return claims.floorKey(key); }
    public String ceilingKey(String key) { return claims.ceilingKey(key); }
    public Map<String, Claim> subMap(String from, String to) { return Map.copyOf(claims.subMap(from, to)); }
}


