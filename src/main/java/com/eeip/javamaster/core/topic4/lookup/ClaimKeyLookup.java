package com.eeip.javamaster.core.topic4.lookup;

import com.eeip.javamaster.core.topic4.domain.Claim;
import com.eeip.javamaster.core.topic4.identity.ClaimKey;

import java.util.HashMap;
import java.util.Map;

/** Demonstrates that equivalent immutable keys find the same HashMap entry. */
public final class ClaimKeyLookup {
    private final Map<ClaimKey, Claim> claims = new HashMap<>();

    public void put(ClaimKey key, Claim claim) { claims.put(key, claim); }
    public Claim get(ClaimKey key) { return claims.get(key); }
    public int size() { return claims.size(); }
}

