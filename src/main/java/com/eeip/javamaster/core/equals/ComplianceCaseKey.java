package com.eeip.javamaster.core.equals;

import java.util.Objects;

/**
 * Composite immutable business identity for a compliance case.
 *
 * A case is uniquely identified by tenantId + caseNumber.
 */
public final class ComplianceCaseKey {

    private final String tenantId;
    private final String caseNumber;

    public ComplianceCaseKey(
            String tenantId,
            String caseNumber
    ) {
        this.tenantId = Objects.requireNonNull(
                tenantId,
                "tenantId must not be null"
        );

        this.caseNumber = Objects.requireNonNull(
                caseNumber,
                "caseNumber must not be null"
        );
    }

    public String tenantId() {
        return tenantId;
    }

    public String caseNumber() {
        return caseNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ComplianceCaseKey that)) {
            return false;
        }

        return tenantId.equals(that.tenantId)
                && caseNumber.equals(that.caseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, caseNumber);
    }
}