package com.eeip.javamaster.core.topic3.list;

import com.eeip.javamaster.core.topic3.domain.ClaimEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/** Ordered event history: List preserves duplicates and supports indexed access. */
public final class ClaimHistory implements Iterable<ClaimEvent> {
    private final List<ClaimEvent> events = new ArrayList<>();

    public void add(ClaimEvent event) { events.add(Objects.requireNonNull(event, "event must not be null")); }
    public ClaimEvent eventAt(int index) { return events.get(index); }
    public int size() { return events.size(); }
    public List<ClaimEvent> snapshot() { return List.copyOf(events); }
    @Override public Iterator<ClaimEvent> iterator() { return events.iterator(); }
}

