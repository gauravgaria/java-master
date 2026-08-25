package com.eeip.javamaster.core.topic3;

import com.eeip.javamaster.core.topic3.domain.*;
import com.eeip.javamaster.core.topic3.examples.bad.IncorrectClaimIdentity;
import com.eeip.javamaster.core.topic3.immutable.ClaimSnapshot;
import com.eeip.javamaster.core.topic3.immutable.CollectionBoundaries;
import com.eeip.javamaster.core.topic3.list.ClaimHistory;
import com.eeip.javamaster.core.topic3.list.ClaimReviewTrail;
import com.eeip.javamaster.core.topic3.map.ClaimIndex;
import com.eeip.javamaster.core.topic3.map.OrderedClaimIndex;
import com.eeip.javamaster.core.topic3.map.SortedClaimIndex;
import com.eeip.javamaster.core.topic3.queue.ClaimProcessingQueue;
import com.eeip.javamaster.core.topic3.queue.PriorityClaimQueue;
import com.eeip.javamaster.core.topic3.set.RiskIndicatorRegistry;
import com.eeip.javamaster.core.topic3.sorting.ClaimComparators;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsLabTest {
    private static Claim claim(String number, ClaimPriority priority, int risk, String amount) {
        return new Claim(number, new BigDecimal(amount), LocalDate.of(2026, 1, 1),
                ClaimRiskLevel.MEDIUM, risk, priority);
    }

    @Test
    void historyPreservesOrderDuplicatesAndIndexedAccess() {
        ClaimHistory history = new ClaimHistory();
        ClaimEvent submitted = new ClaimEvent(ClaimStatus.SUBMITTED, Instant.parse("2026-01-01T00:00:00Z"));
        history.add(submitted);
        history.add(submitted);
        history.add(new ClaimEvent(ClaimStatus.VALIDATED, Instant.parse("2026-01-02T00:00:00Z")));
        assertEquals(List.of(submitted, submitted), List.of(history.eventAt(0), history.eventAt(1)));
        assertEquals(3, history.size());
        assertEquals(3, count(history.iterator()));
    }

    private static int count(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) { iterator.next(); count++; }
        return count;
    }

    @Test
    void linkedListTrailUsesBothEndsForReviewWorkflow() {
        ClaimReviewTrail trail = new ClaimReviewTrail();
        Claim routine = claim("CLM-2", ClaimPriority.LOW, 1, "10");
        Claim urgent = claim("CLM-1", ClaimPriority.HIGH, 2, "20");
        trail.addRoutine(routine);
        trail.addUrgent(urgent);
        assertEquals(urgent, trail.nextReview());
        assertEquals(routine, trail.completeRoutine());
    }

    @Test
    void riskSetDeduplicatesAndOffersMembershipOperations() {
        RiskIndicatorRegistry registry = new RiskIndicatorRegistry();
        assertTrue(registry.add(RiskIndicator.HIGH_CLAIM_AMOUNT));
        assertFalse(registry.add(RiskIndicator.HIGH_CLAIM_AMOUNT));
        assertTrue(registry.contains(RiskIndicator.HIGH_CLAIM_AMOUNT));
        assertTrue(registry.remove(RiskIndicator.HIGH_CLAIM_AMOUNT));
        assertFalse(registry.contains(RiskIndicator.HIGH_CLAIM_AMOUNT));
    }

    @Test
    void setImplementationsHaveDifferentOrderingContracts() {
        Set<String> hash = new HashSet<>(List.of("B", "A", "C"));
        Set<String> insertion = new LinkedHashSet<>(List.of("B", "A", "C"));
        Set<String> sorted = new java.util.TreeSet<>(List.of("B", "A", "C"));
        assertEquals(List.of("B", "A", "C"), new ArrayList<>(insertion));
        assertEquals(List.of("A", "B", "C"), new ArrayList<>(sorted));
        assertEquals(Set.of("A", "B", "C"), hash);
    }

    @Test
    void mapsProvideLookupInsertionOrderAndNavigation() {
        Claim first = claim("CLM-002", ClaimPriority.LOW, 1, "10");
        Claim second = claim("CLM-001", ClaimPriority.HIGH, 8, "20");
        ClaimIndex index = new ClaimIndex();
        index.put(first);
        assertSame(first, index.get("CLM-002"));
        assertTrue(index.containsKey("CLM-002"));
        assertNull(index.get("missing"));
        assertSame(first, index.getOrCreate("CLM-002", () -> second));
        assertSame(first, index.remove("CLM-002"));

        OrderedClaimIndex ordered = new OrderedClaimIndex();
        ordered.put(first); ordered.put(second);
        assertEquals(List.of(first, second), iterableToList(ordered.inInsertionOrder()));

        SortedClaimIndex sorted = new SortedClaimIndex();
        sorted.put(first); sorted.put(second);
        assertEquals("CLM-001", sorted.firstKey());
        assertEquals("CLM-002", sorted.lastKey());
        assertEquals("CLM-001", sorted.floorKey("CLM-001X"));
        assertEquals("CLM-002", sorted.ceilingKey("CLM-001X"));
        assertEquals(Set.of("CLM-001"), sorted.subMap("CLM-001", "CLM-002").keySet());
    }

    private static List<Claim> iterableToList(Iterable<Claim> claims) {
        List<Claim> result = new ArrayList<>();
        claims.forEach(result::add);
        return result;
    }

    @Test
    void arrayDequeIsFifoAndPriorityQueueReturnsBusinessPriorityAtHead() {
        Claim low = claim("LOW", ClaimPriority.LOW, 1, "1");
        Claim high = claim("HIGH", ClaimPriority.HIGH, 1, "1");
        ClaimProcessingQueue fifo = new ClaimProcessingQueue();
        assertTrue(fifo.offer(low)); fifo.offer(high);
        assertSame(low, fifo.peek());
        assertSame(low, fifo.poll());
        assertSame(high, fifo.poll());
        assertNull(fifo.poll());

        PriorityClaimQueue priority = new PriorityClaimQueue();
        priority.offer(low); priority.offer(high);
        assertSame(high, priority.peek());
        assertSame(high, priority.poll());
        assertSame(low, priority.poll());
    }

    @Test
    void comparableAndComparatorsRepresentDifferentOrders() {
        Claim lowNumber = claim("B", ClaimPriority.LOW, 1, "100");
        Claim highAmount = claim("A", ClaimPriority.LOW, 9, "200");
        List<Claim> claims = new ArrayList<>(List.of(lowNumber, highAmount));
        claims.sort(null);
        assertEquals("A", claims.get(0).claimNumber());
        claims.sort(ClaimComparators.byAmountDescending());
        assertEquals("A", claims.get(0).claimNumber());
        claims.sort(ClaimComparators.byRiskScoreDescending());
        assertEquals("A", claims.get(0).claimNumber());
    }

    @Test
    void iteratorSupportsSafeRemoval() {
        List<String> statuses = new ArrayList<>(List.of("SUBMITTED", "REJECTED", "APPROVED"));
        Iterator<String> iterator = statuses.iterator();
        while (iterator.hasNext()) if ("REJECTED".equals(iterator.next())) iterator.remove();
        assertEquals(List.of("SUBMITTED", "APPROVED"), statuses);
    }

    @Test
    void claimIdentityMakesHashSetDeduplicateClaims() {
        Claim first = claim("SAME", ClaimPriority.LOW, 1, "10");
        Claim equivalent = claim("SAME", ClaimPriority.HIGH, 9, "99");
        assertEquals(first, equivalent);
        assertEquals(first.hashCode(), equivalent.hashCode());
        assertEquals(1, new HashSet<>(List.of(first, equivalent)).size());
    }

    @Test
    void brokenHashCodePreventsHashSetDeduplication() {
        Set<IncorrectClaimIdentity> claims = new HashSet<>(List.of(
                new IncorrectClaimIdentity("SAME"),
                new IncorrectClaimIdentity("SAME")
        ));
        assertEquals(2, claims.size());
    }

    @Test
    void immutableBoundariesCopySourcesAndRejectMutation() {
        List<String> source = new ArrayList<>(List.of("A"));
        List<String> snapshot = CollectionBoundaries.immutableSnapshot(source);
        source.add("B");
        assertEquals(List.of("A"), snapshot);
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add("C"));
        assertThrows(UnsupportedOperationException.class, () -> new ClaimSnapshot(List.of(claim("A", ClaimPriority.LOW, 1, "1"))).claims().clear());

        String[] array = {"A"};
        List<String> fixed = CollectionBoundaries.fixedSizeView(array);
        array[0] = "CHANGED";
        assertEquals("CHANGED", fixed.get(0));
        assertThrows(UnsupportedOperationException.class, () -> fixed.add("B"));
        assertEquals(List.of("A", "B"), CollectionBoundaries.immutableFactory("A", "B"));
        assertEquals(List.of("A"), CollectionBoundaries.unmodifiableView(List.of("A")));
    }

    @Test
    void emptyCollectionsAndNullPolicyAreExplicit() {
        assertTrue(new ArrayDeque<>().isEmpty());
        assertThrows(NullPointerException.class, () -> new ClaimHistory().add(null));
        assertThrows(NullPointerException.class, () -> new Claim("X", null, LocalDate.now(), ClaimRiskLevel.LOW, 0, ClaimPriority.LOW));
        assertThrows(NullPointerException.class, () -> List.copyOf(Arrays.asList("A", null)));
    }
}


