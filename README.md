# Java Master

> A production-oriented Java backend engineering laboratory focused on building Senior-level engineering depth through real-world implementation, experimentation, debugging, performance analysis, and interview-driven learning.

## Overview

**Java Master** is a long-term engineering laboratory for developing the depth expected from a Senior Java Backend Engineer.

This repository is intentionally different from a collection of Java interview questions.

The goal is to understand:

* how Java works internally
* why particular engineering decisions are made
* how Java applications behave under production load
* how systems fail
* how to diagnose production problems
* how to design for scalability and reliability
* how to write maintainable production-grade code
* how to communicate engineering trade-offs in senior-level interviews

The examples use realistic enterprise scenarios, particularly around **insurance, healthcare, claims, risk, fraud, compliance, and regulatory workflows**.

---

## Engineering Philosophy

The repository follows one principle:

> **Don't learn technology as syntax. Learn it as an engineering tool.**

For every important concept, the learning process connects:

```text
Concept
   ↓
Why it exists
   ↓
Internal mechanics
   ↓
Production use case
   ↓
Design trade-offs
   ↓
Failure modes
   ↓
Performance
   ↓
Debugging
   ↓
Testing
   ↓
Scalability
   ↓
Production implementation
```

The objective is to develop engineering judgment rather than memorize interview answers.

---

## What This Repository Covers

### Core Java

* Object model
* `equals()` and `hashCode()`
* Object identity
* Immutability
* Defensive copying
* Collections
* HashMap internals
* HashSet
* TreeMap / TreeSet
* ConcurrentHashMap
* Generics
* Streams
* Lambdas
* Functional interfaces
* Exception handling
* Optional
* Modern Java features

### JVM

* JVM architecture
* Heap and stack
* Metaspace
* Object allocation
* Garbage collection
* G1 GC
* Memory leaks
* OutOfMemoryError
* JIT compilation
* Class loading
* Safepoints
* JVM diagnostics
* Heap analysis
* Thread dumps
* Java Flight Recorder
* Performance tuning

### Concurrency

* Java Memory Model
* Happens-before
* Visibility
* Atomicity
* Race conditions
* `volatile`
* Synchronization
* Locks
* Atomic classes
* CAS
* ExecutorService
* Thread pools
* CompletableFuture
* Virtual threads
* Deadlocks
* Thread starvation
* Backpressure

### Spring

* Spring IoC
* Dependency Injection
* Bean lifecycle
* Bean scopes
* Spring proxies
* AOP
* Transactions
* Spring Boot internals
* Auto-configuration
* Spring MVC
* Filters
* Interceptors
* Validation
* Exception handling
* Actuator

### Security

* Authentication
* Authorization
* Spring Security
* JWT
* OAuth2
* Resource servers
* Service-to-service security
* CORS
* CSRF
* Token validation
* Key rotation

### Hibernate / JPA

* Persistence Context
* Entity lifecycle
* Dirty checking
* Lazy loading
* N+1 queries
* Fetch joins
* Entity graphs
* Optimistic locking
* Pessimistic locking
* Batch processing
* Query optimization
* First-level cache
* Second-level cache

### Microservices

* Service boundaries
* API Gateway
* Service discovery
* Configuration
* Synchronous communication
* Event-driven architecture
* Timeouts
* Retries
* Circuit breakers
* Bulkheads
* Rate limiting
* Idempotency
* Distributed transactions
* Saga
* Outbox pattern
* CQRS
* Distributed tracing

### Kafka

* Topics
* Partitions
* Producers
* Consumers
* Consumer groups
* Offsets
* Rebalancing
* Ordering
* Delivery semantics
* Idempotent producers
* Transactions
* Retry strategies
* Dead-letter queues
* Consumer lag
* Partition strategies
* Schema evolution

### Redis / Caching

* Cache-aside
* TTL
* Eviction
* Cache invalidation
* Cache stampede
* Cache penetration
* Hot keys
* Distributed locks
* Redis data structures
* Redis Cluster
* Replication

### PostgreSQL

* Transactions
* ACID
* Isolation levels
* MVCC
* Indexes
* Composite indexes
* Query planning
* `EXPLAIN ANALYZE`
* Locks
* Deadlocks
* Connection pools
* Pagination
* Keyset pagination
* Partitioning
* Replication
* Query optimization

### Containers & Kubernetes

* Docker
* Container images
* Docker networking
* Kubernetes Pods
* Deployments
* Services
* ConfigMaps
* Secrets
* Readiness probes
* Liveness probes
* Startup probes
* Resource requests/limits
* HPA
* Rolling deployments
* Graceful shutdown
* Scheduling
* Network policies

### Production Engineering

* Structured logging
* Correlation IDs
* Metrics
* Distributed tracing
* OpenTelemetry
* SLIs
* SLOs
* SLAs
* Error budgets
* Alerting
* Incident response
* Production debugging
* Capacity planning
* Load testing
* Performance testing

### Reliability & Scalability

* Horizontal scaling
* Vertical scaling
* Load balancing
* Stateless services
* Backpressure
* Connection pool sizing
* Thread pool sizing
* Caching strategies
* Database scaling
* Kafka scaling
* Fault tolerance
* Graceful degradation
* High availability
* Disaster recovery
* Cost optimization

---

# Learning Strategy

The repository is developed in phases.

Each phase is completed in priority order:

```text
Phase
  │
  ├── High Priority
  │      │
  │      └── Complete all topics
  │
  ├── Medium Priority
  │      │
  │      └── Complete all topics
  │
  └── Lower Priority
         │
         └── Complete relevant topics
```

This prevents spending excessive time on low-frequency topics before establishing the production fundamentals expected from a Senior Backend Engineer.

---

# Project Structure

```text
java-master/
│
├── pom.xml
├── README.md
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/eeip/javamaster/
│   │
│   └── test/
│       └── java/
│           └── com/eeip/javamaster/
│
└── docs/
    └── phase-01/
```

Java implementation follows the standard Maven project structure.

Topics are organized primarily through packages so that the project remains easy to build, test, and eventually integrate into CI/CD.

---

# Domain-Driven Examples

The examples intentionally use realistic enterprise domains rather than toy examples.

Typical scenarios include:

```text
Insurance Claims
       │
       ├── Claim Processing
       ├── Adjudication
       ├── Fraud Detection
       ├── Risk Assessment
       ├── Policy Validation
       └── Payment Processing

Healthcare
       │
       ├── Member
       ├── Provider
       ├── Diagnosis
       ├── Procedure
       └── Claims

Compliance
       │
       ├── Regulatory Cases
       ├── Risk Classification
       ├── Compliance Reviews
       ├── Audit Events
       └── Investigation Workflows
```

These domains provide realistic constraints around identity, transactions, concurrency, event processing, data consistency, security, auditability, and reliability.

---

# Engineering Practices

Implementations aim to demonstrate:

* SOLID principles
* Encapsulation
* Composition over inheritance
* Appropriate design patterns
* Immutability where appropriate
* Clear domain boundaries
* Explicit error handling
* Testability
* Observability
* Performance awareness
* Thread-safety
* Failure handling
* Scalability considerations
* Maintainability

The repository deliberately avoids unnecessary abstractions and frameworks when they do not contribute to the concept being demonstrated.

---

# Testing Strategy

Testing evolves with the topic.

Examples include:

* Unit tests
* Integration tests
* Contract tests
* Concurrency tests
* Performance tests
* Load tests
* Failure testing
* Resilience testing

The goal is not simply to achieve code coverage.

The goal is to verify **behavior and engineering guarantees**.

---

# Performance & Production Thinking

Where appropriate, implementations examine:

```text
CPU
Memory
Allocation rate
GC pressure
Thread usage
Lock contention
Database connections
Network calls
Cache behavior
Latency
Throughput
```

Production debugging exercises will use tools such as:

```text
jstack
jcmd
JFR
heap dumps
GC logs
Maven
JUnit
Docker
Kubernetes
```

Additional observability and performance tooling will be introduced as the relevant phases are reached.

---

# Current Progress

## Phase 1 — Core Java

### High Priority

| Topic                                | Status         |
| ------------------------------------ | -------------- |
| `equals()` / `hashCode()` / Identity | ✅ Complete     |
| Immutability & Defensive Copying     | 🚧 In Progress |
| Collections                          | ⏳              |
| HashMap Internals                    | ⏳              |
| HashSet / TreeMap / TreeSet          | ⏳              |
| ConcurrentHashMap                    | ⏳              |
| Generics                             | ⏳              |
| Streams                              | ⏳              |
| Lambdas / Functional Interfaces      | ⏳              |
| Exception Handling                   | ⏳              |
| Optional                             | ⏳              |
| Modern Java Features                 | ⏳              |

Progress will be updated as the laboratory evolves.

---

# Technology Baseline

Current baseline:

* Java 21 LTS
* Maven
* JUnit 5
* Git
* GitHub

Additional technologies will be introduced only when they become relevant to the corresponding phase.

---

# Running the Project

Run the complete test suite:

```bash
mvn clean test
```

Compile the project:

```bash
mvn clean compile
```

The project intentionally starts with minimal dependencies.

Frameworks and infrastructure will be introduced progressively as the learning reaches Spring, databases, Kafka, containers, Kubernetes, and cloud topics.

---

# Repository Philosophy

This repository is a record of the progression from:

```text
Java Developer
      ↓
Strong Java Engineer
      ↓
Senior Backend Engineer
      ↓
Production-focused Engineer
      ↓
Architectural & Staff-level thinking
```

The objective is not to demonstrate that every Java API has been memorized.

The objective is to demonstrate the ability to:

> **Understand a system, make engineering decisions, reason about trade-offs, implement reliable software, operate it in production, diagnose failures, and explain those decisions clearly.**

---

## Status

🚧 **Actively being developed**

The repository is intentionally built incrementally. Each completed topic represents both a learning milestone and a practical implementation exercise.
