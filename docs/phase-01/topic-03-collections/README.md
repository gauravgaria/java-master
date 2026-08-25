# Phase 1 / Topic 3 — Java Collections

## Objective

This small claims-processing lab demonstrates choosing a collection from its business semantics—not from habit. Open the classes under `com.eeip.javamaster.core.topic3` and run the tests while debugging them.

## Collections covered

- `ClaimHistory` uses `ArrayList` behind `List`: claim events are ordered, duplicates are meaningful, and indexed reads are useful.
- `ClaimReviewTrail` uses `LinkedList` only because this review workflow adds urgent work at the front and completes routine work at the back. It is not a general-purpose faster list.
- `RiskIndicatorRegistry` uses `Set`/`LinkedHashSet`: a risk indicator is membership, so duplicates are rejected. It also exposes explicitly named `HashSet` and `TreeSet` snapshots for unordered and sorted views.
- `ClaimIndex` uses `HashMap<String, Claim>` for expected average O(1) claim-number lookup without an ordering promise.
- `OrderedClaimIndex` uses `LinkedHashMap` when reproducible insertion-order audit output matters.
- `SortedClaimIndex` uses `TreeMap` for sorted claim-number navigation (`firstKey`, `lastKey`, `floorKey`, `ceilingKey`, `subMap`).
- `ClaimProcessingQueue` uses `ArrayDeque` for compact FIFO work (`offer`, `peek`, `poll`); it does not permit `null`.
- `PriorityClaimQueue` uses `PriorityQueue` for priority-at-the-head processing. Its iterator is **not** a sorted traversal; repeatedly polling is required for priority order.
- `Claim` implements `Comparable` with stable claim-number natural order. `ClaimComparators` provides alternate amount, date, and risk-score orders without coupling every business sort to the entity.
- `ClaimHistory` implements `Iterable`; the test shows `Iterator.remove()` as the safe single-threaded removal mechanism.

The `examples.bad` package intentionally isolates common mistakes: raw types, mutable collection exposure, enhanced-for removal, and an equality/hash-code violation.

## Complexity and trade-offs

| Collection | Typical operation | Expected complexity | Ordering / duplicates | Main trade-off |
|---|---|---:|---|---|
| `ArrayList` | indexed read / append | O(1) / amortized O(1) | insertion order / yes | contiguous backing array; middle insertion shifts elements |
| `LinkedList` | add/remove at an end | O(1) | insertion order / yes | node allocations and pointer chasing; indexed access O(n) |
| `HashSet` | membership | expected O(1) | no order / no | hashing and resizing; worst cases depend on collisions |
| `LinkedHashSet` | membership | expected O(1) | insertion order / no | linked-order metadata costs memory |
| `TreeSet` | membership / navigation | O(log n) | sorted / no | tree node overhead; requires comparable values or comparator |
| `HashMap` | lookup | expected O(1) | no order / keys unique | hashing, resizing, and no deterministic iteration contract |
| `LinkedHashMap` | lookup / iteration | expected O(1) | insertion order / keys unique | extra links; deterministic output |
| `TreeMap` | lookup / range | O(log n) | sorted keys / keys unique | slower than hash lookup; enables range navigation |
| `ArrayDeque` | end operations | O(1) amortized | deque order / yes | array resizing; not thread-safe |
| `PriorityQueue` | head / insert | O(1) / O(log n) | head prioritized, iterator unsorted | arbitrary removal is expensive; not a fully sorted collection |

These are normal-case/typical costs, not guarantees that every hash operation is always O(1).

## equals/hashCode connection

`Claim` uses immutable `claimNumber` as business identity because it exists before persistence. Equal claims must return the same hash code. Consequently, a `HashSet` stores two claim objects with the same number once, and a `HashMap` can find a value with an equivalent key. `examples.bad.IncorrectClaimIdentity` demonstrates why overriding `equals` without a matching hash-code implementation breaks hash-based collections.

## Immutability connection

`CollectionBoundaries` contrasts `List.of`, `List.copyOf`, `Arrays.asList`, and `Collections.unmodifiableList`. `List.copyOf` creates an immutable snapshot and rejects null elements. An unmodifiable view prevents mutation through that reference but still reflects changes to its source. `Arrays.asList` is a fixed-size, array-backed view—not an immutable copy. `ClaimSnapshot` uses `List.copyOf` at its boundary.

## Common mistakes

- Assuming `HashMap` or `HashSet` iteration is stable.
- Choosing `LinkedList` for ordinary indexed reads.
- Repeating `List.contains` in a large membership loop instead of using a suitable `Set`.
- Removing directly from an enhanced-for loop; use `Iterator.remove()` or a deliberate bulk operation.
- Treating fail-fast `ConcurrentModificationException` behavior as thread safety—it is only a best-effort bug detector.
- Returning internal mutable lists, using raw `Collection` types, or keeping unbounded in-memory claim history.
- Using a local set for cross-instance deduplication: distributed uniqueness belongs in a database constraint or shared system such as Redis, not one JVM's heap.

## Memory, GC, and scale

`ArrayList` stores references contiguously, which generally improves cache locality and uses fewer objects than `LinkedList`. `LinkedList` allocates a node per element; its links increase memory, pointer chasing, and GC work. At approximately 50,000 requests/sec, repeated O(n) list membership, per-request allocation, and unbounded retention can consume CPU and heap quickly. Bounded queues, pagination, streaming, and explicit eviction are often more important than swapping one collection for another.

At 100 users, an in-memory bounded list may be adequate. At 10,000 or 100,000 users, bound per-request state and index only what is needed. At 1 million, pagination/database indexes and shared caches are usually preferable to retaining every claim in each instance. At 50 million, durable storage, streaming/Kafka-like processing, database uniqueness, and distributed caches may be architectural requirements. Redis can provide shared deduplication, while a database constraint provides durable uniqueness; neither is solved by replacing `ArrayList` with `HashSet`.

## Run tests

From the repository root:

```bash
mvn clean test
```

The tests verify behavior for ordering, uniqueness, equality, map navigation, FIFO and priority processing, comparator sorting, iterator removal, null policy, defensive copying, and immutable collection mutation.

