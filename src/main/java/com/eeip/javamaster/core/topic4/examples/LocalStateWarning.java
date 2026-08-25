package com.eeip.javamaster.core.topic4.examples;

/**
 * Documentation marker for an architectural constraint: a HashMap belongs to one JVM.
 * It is not a distributed cache, durable store, or cross-pod deduplication mechanism.
 */
public final class LocalStateWarning {
    private LocalStateWarning() { }
}
