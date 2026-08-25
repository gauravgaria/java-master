package com.eeip.javamaster.core.topic3.immutable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Examples of ownership boundaries; callers must not receive mutable domain state. */
public final class CollectionBoundaries {
    private CollectionBoundaries() { }

    public static List<String> immutableSnapshot(List<String> source) { return List.copyOf(source); }
    public static List<String> immutableFactory(String first, String second) { return List.of(first, second); }
    public static List<String> fixedSizeView(String[] source) { return Arrays.asList(source); }
    public static List<String> unmodifiableView(List<String> source) { return Collections.unmodifiableList(source); }
}
