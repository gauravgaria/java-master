package com.eeip.javamaster.core.topic1;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClaimKeyTest {

    @Test
    void equalClaimKeysMustHaveSameHashCode() {
        ClaimKey first = new ClaimKey("CLM-2026-0001");
        ClaimKey second = new ClaimKey("CLM-2026-0001");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentClaimNumbersMustNotBeEqual() {
        ClaimKey first = new ClaimKey("CLM-2026-0001");
        ClaimKey second = new ClaimKey("CLM-2026-0002");

        assertNotEquals(first, second);
    }

    @Test
    void equivalentClaimKeyShouldFindExistingHashMapEntry() {
        ClaimKey storedKey = new ClaimKey("CLM-2026-0001");
        ClaimKey lookupKey = new ClaimKey("CLM-2026-0001");

        Map<ClaimKey, String> claims = new HashMap<>();

        claims.put(storedKey, "CLAIM_PROCESSED");

        assertEquals("CLAIM_PROCESSED", claims.get(lookupKey));
    }

    @Test
    void equivalentClaimKeysShouldBeDeduplicatedByHashSet() {
        ClaimKey first = new ClaimKey("CLM-2026-0001");
        ClaimKey second = new ClaimKey("CLM-2026-0001");

        Set<ClaimKey> claims = new HashSet<>();

        claims.add(first);
        claims.add(second);

        assertEquals(1, claims.size());
    }

    @Test
    void nullClaimNumberShouldBeRejected() {
        assertThrows(
                NullPointerException.class,
                () -> new ClaimKey(null)
        );
    }
}