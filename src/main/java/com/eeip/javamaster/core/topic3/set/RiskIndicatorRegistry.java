package com.eeip.javamaster.core.topic3.set;

import com.eeip.javamaster.core.topic3.domain.RiskIndicator;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/** Set models membership: duplicate risk flags have no business meaning. */
public final class RiskIndicatorRegistry {
    private final Set<RiskIndicator> indicators = new LinkedHashSet<>();

    public boolean add(RiskIndicator indicator) { return indicators.add(Objects.requireNonNull(indicator, "indicator must not be null")); }
    public boolean remove(RiskIndicator indicator) { return indicators.remove(indicator); }
    public boolean contains(RiskIndicator indicator) { return indicators.contains(indicator); }
    public int size() { return indicators.size(); }
    public Set<RiskIndicator> insertionOrderedSnapshot() { return Collections.unmodifiableSet(new LinkedHashSet<>(indicators)); }
    public Set<RiskIndicator> hashSnapshot() { return Collections.unmodifiableSet(new HashSet<>(indicators)); }
    public Set<RiskIndicator> sortedSnapshot() { return Collections.unmodifiableSet(new TreeSet<>(indicators)); }
}
