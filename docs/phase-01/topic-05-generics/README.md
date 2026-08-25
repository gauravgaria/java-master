# Topic 05 — Generics

## Why generics exist

Generics provide compile-time type safety and reusable APIs, avoiding `Object`, unsafe casts, and duplicated implementations.

## Example concepts

- **Generic class:** `ProcessingResult<T>` can hold either `Claim` or `RiskAssessment` without casts.
- **Generic method:** `ClaimProcessor.first(List<? extends T>)` works with different value types.
- **Bounded type:** `<T extends Number>` permits numeric types and supports an amount calculation.
- **Invariance:** `List<Claim>` is not a `List<Object>`; generic types are not interchangeable just because their type arguments have an inheritance relationship.
- **`? extends`:** a producer can be safely read.
- **`? super`:** a consumer can safely receive values.
- **PECS:** Producer Extends, Consumer Super.
- **Type erasure:** generic parameters are primarily enforced at compile time and erased for runtime. `instanceof List<Claim>` is invalid; use `instanceof List<?>` when a runtime list check is needed.

## EIIP connection

Claims, risk assessments, policies, and compliance cases can share type-safe processing APIs without `Object` everywhere or repeated implementations. The main production value is safety, maintainability, and reuse—not automatic speed improvement.

At 50K+ RPS, scalability still depends on memory, allocation, databases, networks, caching, concurrency, and downstream capacity.

## Common mistakes

Avoid raw collections (`List claims`), `Object` results, unnecessary casts, and generic abstractions that add no type-safety value.

## Run tests

```bash
mvn clean test
```

