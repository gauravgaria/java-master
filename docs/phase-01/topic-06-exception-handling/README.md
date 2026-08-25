# Topic 06 — Exception Handling

## What this demonstrates

A small EIIP claim-processing flow showing custom exceptions, propagation, translation, cause preservation, safe logging, and automatic resource cleanup.

## Key ideas

- **Checked vs unchecked:** `IOException` represents a low-level checked failure; `ClaimProcessingException` is an unchecked application failure.
- **Exception translation:** `ClaimDataReader` converts an `IOException` into `ClaimProcessingException` at the application boundary.
- **Cause preservation:** the original exception is passed as the cause, retaining diagnostic context.
- **Try-with-resources:** the claim resource is closed automatically after both successful and failed reads.
- **Normal vs exceptional outcomes:** a missing claim returns `Optional.empty()`; an infrastructure failure throws.
- **Production logging:** log a safe claim identifier and the exception object once. Do not log full claims, sensitive data, or stack traces converted to strings.

## EIIP connection

```text
claim processing
    ↓
resource/external operation
    ↓
low-level failure
    ↓
exception translation
    ↓
application-level failure
    ↓
caller decides: retry, reject, or fail
```

Actual retry, transaction management, REST exception handling, Kafka error handling, and resilience belong to later Spring, microservices, and Kafka topics.

## Common mistakes

Do not swallow exceptions, replace a cause while wrapping, catch `Throwable` for ordinary application failures, manually close resources when try-with-resources applies, or use exceptions for normal control flow.

At 50K+ RPS, exceptions used as normal control flow add allocation/CPU pressure, resource leaks can exhaust capacity, uncontrolled errors can create logging storms, and sensitive data must never be dumped into logs.

## Run tests

```bash
mvn clean test
```

