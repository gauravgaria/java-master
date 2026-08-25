package com.eeip.javamaster.core.topic4;

import com.eeip.javamaster.core.topic4.basic.ClaimHashMapIndex;
import com.eeip.javamaster.core.topic4.capacity.HashMapCapacityExample;
import com.eeip.javamaster.core.topic4.domain.Claim;
import com.eeip.javamaster.core.topic4.domain.ClaimStatus;
import com.eeip.javamaster.core.topic4.examples.HashCollisionExample;
import com.eeip.javamaster.core.topic4.examples.MutableKeyProblem;
import com.eeip.javamaster.core.topic4.examples.bad.IncorrectClaimKey;
import com.eeip.javamaster.core.topic4.identity.ClaimKey;
import com.eeip.javamaster.core.topic4.lookup.ClaimKeyLookup;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HashMapInternalsLabTest {
    private static Claim claim(String number) {
        return new Claim(number, "MEM-1", new BigDecimal("125.00"), 7, ClaimStatus.SUBMITTED);
    }

    @Test
    void indexSupportsPutGetReplaceRemoveContainsAndSize() {
        ClaimHashMapIndex index = new ClaimHashMapIndex();
        Claim original = claim("CLM-100001");
        Claim replacement = new Claim("CLM-100001", "MEM-2", new BigDecimal("200.00"), 3, ClaimStatus.APPROVED);

        assertNull(index.put(original));
        assertSame(original, index.get("CLM-100001"));
        assertTrue(index.containsKey("CLM-100001"));
        assertSame(original, index.put(replacement));
        assertEquals(1, index.size());
        assertSame(replacement, index.remove("CLM-100001"));
        assertNull(index.get("missing"));
        assertFalse(index.containsKey("CLM-100001"));
        assertEquals(0, index.size());
    }

    @Test
    void equivalentImmutableKeysShareHashCodeAndFindTheEntry() {
        ClaimKey first = new ClaimKey("CLM-100001");
        ClaimKey equivalent = new ClaimKey("CLM-100001");
        ClaimKeyLookup lookup = new ClaimKeyLookup();
        lookup.put(first, claim("CLM-100001"));

        assertEquals(first, equivalent);
        assertEquals(first.hashCode(), equivalent.hashCode());
        assertNotNull(lookup.get(equivalent));
        assertEquals(1, lookup.size());
    }

    @Test
    void claimEqualityUsesBusinessIdentityNotMutableClaimDetails() {
        Claim first = claim("CLM-100001");
        Claim equivalent = new Claim("CLM-100001", "DIFFERENT-MEMBER", new BigDecimal("999.00"), 99, ClaimStatus.APPROVED);
        assertEquals(first, equivalent);
        assertEquals(first.hashCode(), equivalent.hashCode());
    }

    @Test
    void collisionDoesNotMakeDifferentKeysEqual() {
        HashCollisionExample keyA = new HashCollisionExample("A");
        HashCollisionExample keyB = new HashCollisionExample("B");
        Map<HashCollisionExample, String> values = new HashMap<>();
        values.put(keyA, "value-a");
        values.put(keyB, "value-b");

        assertEquals(keyA.hashCode(), keyB.hashCode());
        assertNotEquals(keyA, keyB);
        assertEquals("value-a", values.get(new HashCollisionExample("A")));
        assertEquals("value-b", values.get(keyB));
        assertEquals(2, values.size());
    }

    @Test
    void mutableKeyBecomesEffectivelyUnreachableAfterMutation() {
        MutableKeyProblem key = new MutableKeyProblem("CLM-100001");
        Map<MutableKeyProblem, Claim> claims = new HashMap<>();
        claims.put(key, claim("CLM-100001"));

        key.changeClaimNumber("CLM-999999");

        assertNull(claims.get(key));
        assertNull(claims.get(new MutableKeyProblem("CLM-100001")));
        assertEquals(1, claims.size());
    }

    @Test
    void incorrectHashCodeBreaksEquivalentKeyLookup() {
        IncorrectClaimKey stored = new IncorrectClaimKey("CLM-100001");
        IncorrectClaimKey equivalent = new IncorrectClaimKey("CLM-100001");
        Map<IncorrectClaimKey, String> claims = new HashMap<>();
        claims.put(stored, "stored");

        assertEquals(stored, equivalent);
        assertNotEquals(stored.hashCode(), equivalent.hashCode());
        assertNull(claims.get(equivalent));
    }

    @Test
    void hashmapAllowsOneNullKeyButClaimKeyRejectsNullBusinessIdentity() {
        Map<String, Claim> claims = new HashMap<>();
        claims.put(null, claim("CLM-100001"));
        claims.put(null, claim("CLM-100002"));
        assertEquals(1, claims.size());
        assertEquals("CLM-100002", claims.get(null).claimNumber());
        assertThrows(NullPointerException.class, () -> new ClaimKey(null));
    }

    @Test
    void capacityPlanningValidatesInputsWithoutInspectingPrivateState() {
        assertEquals(0, HashMapCapacityExample.defaultCapacity().size());
        assertEquals(14, HashMapCapacityExample.capacityFor(10, 0.75f));
        assertTrue(HashMapCapacityExample.preSizedFor(10).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> HashMapCapacityExample.capacityFor(1, 1.0f));
        assertThrows(IllegalArgumentException.class, () -> HashMapCapacityExample.preSizedFor(-1));
    }

    @Test
    void emptyMapHasExpectedMissingKeyBehavior() {
        Map<String, Claim> claims = new HashMap<>();
        claims.put("temporary", claim("CLM-100001"));
        claims.clear();
        assertTrue(claims.isEmpty());
        assertNull(claims.get("missing"));
        assertFalse(claims.containsKey("missing"));
        assertNull(claims.remove("missing"));
    }
}





