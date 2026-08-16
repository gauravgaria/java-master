# Topic 01 — `equals()` and `hashCode()` Contract

## Purpose

This document captures the mental models, corrections, examples, and production implications covered while learning Java's `equals()` and `hashCode()` contract.

The focus is not interview memorization. The goal is to understand how logical identity, hashing, `HashMap`, `HashSet`, mutability, and production behavior fit together.

---

# 1. Core Mental Model

The most important distinction is:

```text
equals()  → "Are these two objects logically the same?"
hashCode() → "Which hash-table region should I look in first?"
```

`hashCode()` is **not** the identity check.

`equals()` determines logical equality.

`hashCode()` provides a routing/filtering mechanism used by hash-based collections.

Conceptually:

```text
                         KEY
                          |
                          v
                     hashCode()
                          |
                          v
                  hash processing
                          |
                          v
                     bucket index
                          |
                          v
                  candidate entries
                          |
                          v
                       equals()
                          |
                    +-----+-----+
                    |           |
                  true        false
                    |           |
                  FOUND      try next
```

---

# 2. `equals()` — Logical Equality

Java objects have object identity, but applications frequently need **logical equality**.

Example:

```java
Claim a = new Claim("CLM-1001");
Claim b = new Claim("CLM-1001");
```

These are two different object instances.

```text
a == b
```

is normally:

```text
false
```

because `==` compares object references for objects.

But if `claimId` defines the business identity of a claim:

```text
a.equals(b)
```

should be:

```text
true
```

The business meaning is:

```text
CLM-1001 == CLM-1001
```

even though the Java objects are different instances.

---

# 3. `==` vs `equals()`

For object references:

```java
a == b
```

asks:

> Are these the same object instance?

Whereas:

```java
a.equals(b)
```

asks:

> According to this class's equality definition, do these objects represent the same logical value/entity?

Example:

```text
Object A ───────┐
                │
                ▼
             Claim
             CLM-1001

Object B ───────┐
                │
                ▼
             Claim
             CLM-1001
```

They are different objects but may be logically equal.

---

# 4. The `equals()` Contract

For a well-designed equality implementation, the relationship should be:

### Reflexive

```text
a.equals(a) == true
```

### Symmetric

```text
a.equals(b) == b.equals(a)
```

### Transitive

If:

```text
a.equals(b)
b.equals(c)
```

then:

```text
a.equals(c)
```

### Consistent

Repeated calls should produce the same result as long as the relevant state has not changed.

### Null

```text
a.equals(null) == false
```

These properties matter because collections and other APIs rely on equality being predictable.

---

# 5. `hashCode()` Is Not a Memory Address

A common incorrect mental model is:

```text
object
   ↓
memory address
   ↓
hashCode
```

Do **not** use this model.

Instead:

```text
object's logical state
        |
        v
   hashCode()
        |
        v
    int value
        |
        v
hash processing / bucket calculation
        |
        v
     bucket
```

A hash code is an `int`.

It is not a Java guarantee that the hash code represents a heap address or memory location.

---

# 6. The Fundamental `equals()` / `hashCode()` Contract

The most important rule is:

```text
if a.equals(b) == true
        |
        v
a.hashCode() == b.hashCode()
```

In words:

> If two objects are equal according to `equals()`, they must have the same hash code.

The reverse is **NOT required**:

```text
a.hashCode() == b.hashCode()
        |
        X
does NOT imply
        |
a.equals(b) == true
```

This is one of the most important concepts in the topic.

---

# 7. Why the Reverse Is Not Required

`hashCode()` returns an `int`.

There are only:

```text
2^32
```

possible `int` values.

But Java can represent vastly more possible objects and values.

Therefore, different objects can inevitably produce the same hash code.

This is called a:

# Hash Collision

Example:

```java
@Override
public int hashCode() {
    return claimId.length();
}
```

This is intentionally a poor hash function used only to demonstrate collisions.

Consider:

```text
ClaimKey("CLM-1001")
ClaimKey("CLM-2000")
```

Both strings have the same length.

Therefore:

```text
hashCode(A) == hashCode(B)
```

but:

```text
A.equals(B) == false
```

This is completely legal.

---

# 8. The Three Important Cases

| Case | `equals()` | `hashCode()` | Valid? |
|---|---:|---:|---|
| Same logical value | `true` | Same | Required |
| Different values, different hash | `false` | Different | Normal |
| Different values, same hash | `false` | Same | Valid collision |

The third case is why `HashMap` needs both `hashCode()` and `equals()`.

---

# 9. HashMap — Two-Stage Lookup

Suppose:

```java
Map<ClaimKey, Claim> claims = new HashMap<>();
```

And:

```java
claims.get(new ClaimKey("CLM-2000"));
```

Conceptually, lookup works like:

```text
new ClaimKey("CLM-2000")
             |
             v
         hashCode()
             |
             v
       hash processing
             |
             v
        bucket index
             |
             v
    candidate entries
             |
             v
          equals()
             |
             v
       actual match
```

The hash code narrows down where HashMap should look.

`equals()` determines whether a candidate is actually the requested key.

---

# 10. Collision Example

Suppose two different keys produce the same hash:

```text
ClaimKey("CLM-1001") → hash 8
ClaimKey("CLM-2000") → hash 8
```

They can end up in the same bucket:

```text
Bucket 3
   |
   +-- Entry 1
   |     key = CLM-1001
   |
   +-- Entry 2
         key = CLM-2000
```

Now lookup:

```java
claims.get(new ClaimKey("CLM-2000"));
```

Conceptually:

```text
hashCode()
    |
    v
bucket 3
    |
    +-- CLM-1001
    |      |
    |    equals?
    |      |
    |    false
    |
    +-- CLM-2000
           |
         equals?
           |
          true
           |
         FOUND
```

So:

```text
hashCode() → narrows candidates
equals()   → confirms identity
```

---

# 11. Important Correction: Same Hash Does Not Mean Same Bucket

It is useful to think that the hash determines the bucket, but technically:

```text
hashCode()
    |
    v
hash processing
    |
    v
bucket index based on table state
```

The raw hash code is not itself a bucket number.

The bucket index depends on the hash processing and the current hash-table capacity.

Therefore prefer this mental model:

> The hash code participates in determining the bucket.

rather than:

> The hash code is the bucket.

---

# 12. HashSet and HashMap

`HashSet` is implemented using a hash-based map internally.

Conceptually:

```text
HashSet<ClaimKey>
       |
       v
HashMap<ClaimKey, internal-dummy-value>
```

When you do:

```java
set.add(claimKey);
```

the set needs to determine whether an equivalent key already exists.

Therefore the same fundamental mechanism applies:

```text
hashCode()
    |
    v
candidate bucket
    |
    v
equals()
    |
    v
duplicate or new element
```

This is why incorrect `equals()`/`hashCode()` implementations can break both `HashMap` and `HashSet`.

---

# 13. Business Identity Example

Suppose:

```java
public final class ClaimKey {

    private final String claimId;

    public ClaimKey(String claimId) {
        this.claimId = claimId;
    }

    public String claimId() {
        return claimId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ClaimKey that)) {
            return false;
        }

        return claimId.equals(that.claimId);
    }

    @Override
    public int hashCode() {
        return claimId.hashCode();
    }
}
```

Then:

```text
ClaimKey A → CLM-1001
ClaimKey B → CLM-1001
```

produces:

```text
A.equals(B) == true
A.hashCode() == B.hashCode()
```

This satisfies the contract.

---

# 14. `hashCode()` Does Not Necessarily Use a Unique Identifier

Another important nuance:

> When overriding `hashCode()`, we are not necessarily "providing the unique identifier."

The correct rule is:

> The state used by `hashCode()` must be consistent with the state used by `equals()`.

If claim identity is:

```text
claimId
```

then:

```text
equals()    → claimId
hashCode()  → claimId
```

If logical equality instead depends on:

```text
countryCode + nationalIdentifier
```

then the hash code should incorporate the same logical identity fields.

The important relationship is:

```text
Fields defining equality
        |
        +----> equals()
        |
        +----> hashCode()
```

---

# 15. Mutable Keys — The Major Production Hazard

This is one of the most important production implications of the topic.

Suppose:

```java
class ClaimKey {

    private String claimId;

    // equals() and hashCode() use claimId
}
```

And:

```java
ClaimKey key = new ClaimKey("CLM-1001");

Set<ClaimKey> claims = new HashSet<>();
claims.add(key);
```

At insertion:

```text
claimId = CLM-1001
     |
     v
hashCode()
     |
     v
bucket 4
     |
     v
HashSet
bucket 4 → key
```

Now mutate the key:

```java
key.setClaimId("CLM-9999");
```

Its logical identity has changed.

Now:

```text
claimId = CLM-9999
     |
     v
new hashCode()
     |
     v
bucket 11
```

But the object is still stored in the old bucket.

So:

```text
Current logical identity
        |
        v
    bucket 11

Actual stored location
        |
        v
    bucket 4
```

The collection's internal assumptions are now broken.

---

# 16. The Strange `HashSet.contains()` Failure

After mutation:

```java
claims.contains(key);
```

may return:

```text
false
```

even though the exact same object instance was previously added.

Why?

Because lookup calculates the bucket using the object's **current** hash code.

```text
current key
    |
    v
hashCode()
    |
    v
bucket 11
    |
    v
search bucket 11
    |
    X
object is actually in bucket 4
```

The collection does not normally scan every bucket to recover from this.

This is why mutating fields involved in `equals()`/`hashCode()` while an object is being used as a hash key is dangerous.

---

# 17. Mutable Keys Can Also Produce Logical Duplicates

Consider:

```text
Original key:

CLM-1001
   |
   v
stored in bucket 4
```

Then:

```text
mutate to CLM-2000
```

The object remains in bucket 4.

Now add:

```java
new ClaimKey("CLM-2000")
```

HashSet calculates:

```text
CLM-2000
    |
    v
bucket 11
```

It searches bucket 11 and may not see the mutated original object sitting in bucket 4.

It can therefore accept the new key.

You can end up with multiple objects that violate the application's intended uniqueness assumptions.

---

# 18. Why Immutability Helps

If the fields used by `equals()` and `hashCode()` cannot change:

```text
logical identity
      |
      v
cannot change
      |
      v
hashCode remains stable
      |
      v
hash-based collection remains correctly addressable
```

Example:

```java
public final class ClaimKey {

    private final String claimId;

    public ClaimKey(String claimId) {
        this.claimId = claimId;
    }

    public String claimId() {
        return claimId;
    }
}
```

The important property is not simply:

> "Immutable objects are always good."

The specific property is:

> **State participating in equality and hashing remains stable while the object is used as a hash key.**

---

# 19. Why Stable Identity Matters in Production

Hash-based collections assume that the relationship between a key and its hash remains stable while the key is stored.

A simplified invariant is:

```text
key inserted
    |
    v
hash based on logical identity
    |
    v
bucket chosen
```

That relationship should remain stable.

If:

```text
key identity changes
        |
        v
hash changes
        |
        v
collection's routing assumption becomes stale
```

the collection can behave unexpectedly.

This can lead to:

- failed lookups
- failed `contains()`
- unexpected duplicates
- inability to remove an object
- cache misses
- incorrect deduplication
- inconsistent in-memory state

---

# 20. A Particularly Dangerous Example: Cache Keys

Imagine a service cache:

```java
Map<ClaimKey, ClaimResult> cache = new HashMap<>();
```

If `ClaimKey` is mutable:

```text
Request
  |
  v
ClaimKey("CLM-1001")
  |
  v
cache.put(key, result)
```

Later:

```text
key.claimId changes to CLM-2000
```

The cached entry may still physically reside under the bucket calculated for `CLM-1001`.

Now:

```text
cache.get(new ClaimKey("CLM-2000"))
```

may miss.

This can cause:

```text
cache miss
   |
   v
unnecessary database call
   |
   v
higher latency
   |
   v
higher DB load
```

At high traffic volumes, a bad key design can therefore become a performance problem rather than merely a "Java collections bug."

---

# 21. Production Mental Model

For a domain value such as `ClaimKey`, think:

```text
ClaimKey
   |
   +-- logical identity
   |      |
   |      +--> equals()
   |      |
   |      +--> hashCode()
   |
   +-- must remain stable
          |
          v
    safe hash-key usage
```

This is especially important for:

- `HashMap` keys
- `HashSet` elements
- caches
- deduplication
- lookup indexes
- in-memory registries
- identity maps

---

# 22. Common Incorrect Mental Models

## Incorrect

```text
hashCode = memory address
```

### Correct

```text
hashCode = integer derived according to the class's hashing implementation
```

---

## Incorrect

```text
same hashCode = objects are equal
```

### Correct

```text
same hashCode = objects may be candidates for equality
```

---

## Incorrect

```text
different objects must have different hashCodes
```

### Correct

```text
different objects can have the same hashCode
```

This is a collision.

---

## Incorrect

```text
hashCode itself is the bucket number
```

### Correct

```text
hashCode participates in determining the bucket index
```

---

## Incorrect

```text
hashCode must always use a unique ID
```

### Correct

```text
hashCode must be consistent with equals()
```

---

## Incorrect

```text
immutable object automatically has a correct hashCode
```

### Correct

```text
immutability keeps equality/hash-relevant state stable;
the implementation must still correctly satisfy the contract
```

---

# 23. The Most Important Rules to Remember

### Rule 1

```text
a.equals(b) == true
        =>
a.hashCode() == b.hashCode()
```

### Rule 2

```text
a.hashCode() == b.hashCode()
        does NOT imply
a.equals(b) == true
```

### Rule 3

Different objects can have the same hash code.

That is a collision.

### Rule 4

`HashMap` uses hashing to narrow the search and equality to determine the actual key match.

### Rule 5

`HashSet` relies on the same fundamental equality/hashing mechanism.

### Rule 6

Fields used by `equals()` and `hashCode()` should remain stable while the object is being used as a hash key.

### Rule 7

Immutable value objects are often excellent candidates for hash keys.

---

# 24. Final Mental Model

Keep this model:

```text
                         OBJECT
                            |
                 +----------+----------+
                 |                     |
          logical identity       object identity
                 |
                 v
             equals()
                 |
          "same logical thing?"
                 |
                 v
             hashCode()
                 |
       "same hash region?"
                 |
                 v
          HashMap / HashSet
                 |
                 v
              bucket
                 |
                 v
          candidate entries
                 |
                 v
              equals()
                 |
                 v
          actual logical match
```

The shortest accurate summary is:

> **`equals()` defines logical equality. `hashCode()` provides a stable hash value that helps hash-based collections locate candidate entries efficiently. Equal objects must have equal hash codes, but equal hash codes do not require equal objects.**

---

# 25. Production Checklist

When designing a class that may be used as a `HashMap` key or `HashSet` element, ask:

- Does `equals()` represent the correct business/logical identity?
- Does `hashCode()` use the same logical identity?
- Can the equality-relevant state change after insertion?
- Is the class immutable or otherwise protected from mutation?
- Can the key be safely used in caches?
- Are equality semantics consistent across the application's boundaries?
- Are `equals()` and `hashCode()` tested together?
- Are collision scenarios understood?
- Is the class being used as an entity identity or as a value object?

---

# 26. Topic 01 Summary

The foundation is:

```text
Object identity
      |
      v
Logical equality
      |
      v
equals()
      |
      +-------------------+
      |                   |
      v                   v
same logical object    different
      |                   |
      v                   v
same hash required     hash may still collide
      |
      v
hashCode()
      |
      v
HashMap / HashSet
      |
      v
bucket selection
      |
      v
equals() confirmation
```

The key engineering insight is:

> **Hashing is about efficient routing; equality is about correctness.**

A production-quality implementation must preserve both.

---

## Suggested Revision Exercise

Before moving away from this topic, be able to explain this scenario without referring to notes:

```text
1. Create ClaimKey("CLM-1001")
2. Add it to HashSet
3. Mutate claimId to "CLM-2000"
4. Call contains()
5. Explain why it may return false
6. Add a new ClaimKey("CLM-2000")
7. Explain how duplicate logical entries can arise
8. Explain why immutability prevents the problem
9. Explain why two unequal objects can have the same hashCode()
10. Explain why HashMap still needs equals()
```

If you can reason through those steps naturally, the core mental model for Topic 01 is solid.
