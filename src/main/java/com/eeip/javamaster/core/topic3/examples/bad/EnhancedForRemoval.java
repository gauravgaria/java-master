package com.eeip.javamaster.core.topic3.examples.bad;

import java.util.List;

/** Deliberately bad: structural removal during enhanced-for can throw ConcurrentModificationException. */
public final class EnhancedForRemoval {
    public void removeRejected(List<String> statuses) {
        for (String status : statuses) {
            if ("REJECTED".equals(status)) statuses.remove(status);
        }
    }
}

