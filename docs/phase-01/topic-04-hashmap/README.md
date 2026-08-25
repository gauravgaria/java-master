# Phase 1 / Topic 4 — HashMap Internals

## Objective

This lab explains the behavior and engineering trade-offs behind `HashMap` using a small EIIP claim lookup domain. It focuses on the public contract and observable behavior, not private JDK implementation details.

## Mental model

```text
HashMap
  |
  v
 table
  |
  v
 bucket
  |
  +-- Node(key, value)
  +-- Node(key, value)
  +-- Node(key, value)
```

A simplified lookup is:

```text
key -> hashCode() -> hash spreading -> bucket index
    -> candidate entries -> equals() -> value
```

`hashCode()` narrows the search area. `equals()` confirms the actual logical key. Different keys can share a bucket and hash code; `HashCollisionExample` demonstrates this without pretending to recreate the JDK table.

## Claim lookup and identity

`ClaimHashMapIndex` stores `Map<String, Claim>` and demonstrates `put`, `get`, replacement, `containsKey`, `remove`, and `size`. `ClaimKeyLookup` uses `Map<ClaimKey, Claim>`. `Claim` and `ClaimKey` use validated, immutable `claimNumber` identity; `ClaimKey` is a Java record. Equivalent keys have equal hash codes and therefore retrieve the same entry.

`examples.bad.IncorrectClaimKey` overrides `equals` but returns unrelated hash codes. It violates the contract and can make an equivalent key miss an existing map entry. `examples.MutableKeyProblem` shows the other major failure: mutating a field used by `hashCode()` after insertion leaves the entry in its old bucket, so normal lookup can no longer find it.

## Collisions

```text
key A --hashCode()=42--+
                       +--> same bucket --> equals(A, B) is false --> two entries
key B --hashCode()=42--+
```

A collision is not data loss. It adds candidate comparisons and can degrade lookup when distribution is poor. Java 8+ implementations may treeify heavily-colliding buckets under implementation-defined conditions; the exact thresholds and representation are JDK details and are intentionally not tested here.

## Resizing and load factor

The default load factor commonly used by `HashMap` is `0.75`. Conceptually:

```text
capacity (16) * load factor (0.75) ≈ threshold (12)
entries grow -> threshold reached -> larger table -> entries redistributed
```

`HashMapCapacityExample` contrasts default construction with pre-sizing and provides an approximate planning calculation. Pre-size when a known, bounded dataset is large enough that avoiding repeated growth matters; over-sizing wastes memory. Capacity is not the same as current size, and private table capacity should not be inspected with reflection.

## Complexity and ordering

`get`, `put`, `remove`, and `containsKey` are expected/average O(1) with good hash distribution, not a mathematical guarantee for every input. Collisions, resizing, hashing cost, and memory locality affect real performance. `HashMap` permits one `null` key, but business identifiers are validated and should normally reject null. `HashMap` has no ordering guarantee: use `LinkedHashMap` for insertion order or `TreeMap` for sorted keys.

## Memory and production boundaries

A map is a local JVM data structure. If EIIP runs in four pods, each pod has a different map. Entries can become stale and disappear on restart, and the map is not a shared deduplication mechanism. `UnboundedClaimMap` is intentionally bad: retaining every claim ever seen grows the heap, increases GC pressure and latency, and risks OOM. Bounded caches with eviction, pagination, streaming, a database, Redis, or event-driven state may be appropriate depending on ownership and durability requirements.

At approximately 50,000 lookup requests/sec, an in-process `claimsByNumber.get(claimNumber)` can be fast for bounded local state. It is not a replacement for PostgreSQL, Redis, Kafka, a distributed cache, or persistent storage. Shared uniqueness may require a database constraint or distributed state; changing `HashMap` to `HashSet` does not solve cross-pod consistency.

## Production failure scenarios

- Mutable identity key: insertion succeeds, later lookup misses.
- Broken `equals`/`hashCode`: equivalent claim numbers do not find the same entry.
- Unbounded map: heap growth, GC pauses, latency, and OOM risk.
- Order-dependent code: behavior changes because `HashMap` order is unspecified.
- Distributed-state assumption: pods disagree because each owns a separate heap.
- Poor capacity choice: repeated resizing for a known large set, or wasted memory from excessive pre-sizing.

`examples.bad` contains small, clearly isolated versions of these mistakes. `LocalStateWarning` marks the local-state boundary without implementing infrastructure.

## Common interview mistakes

- Saying HashMap is always O(1).
- Saying a collision means keys are equal.
- Discussing only `hashCode()` and forgetting `equals()`.
- Mutating a key after insertion.
- Treating iteration order as stable.
- Claiming `HashMap` is thread-safe or distributed.
- Using reflection to assert private capacity or treeification thresholds.
- Assuming pre-sizing eliminates all resizing or automatically improves every workload.

## Run tests

From the repository root:

```bash
mvn clean test
```

The tests cover lookup lifecycle, replacement, missing keys, empty maps, null keys, equivalent immutable keys, equality/hash-code violations, deterministic collisions, mutable-key failure, and capacity planning without relying on private implementation details.


