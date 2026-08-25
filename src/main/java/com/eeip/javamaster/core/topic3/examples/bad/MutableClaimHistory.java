package com.eeip.javamaster.core.topic3.examples.bad;

import java.util.ArrayList;
import java.util.List;

/** Deliberately bad: exposing this list lets callers corrupt domain history. */
public final class MutableClaimHistory {
    private final List<String> statuses = new ArrayList<>();
    public List<String> statuses() { return statuses; }
}
