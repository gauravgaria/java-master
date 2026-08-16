package com.eeip.javamaster.core.equals;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComplianceCaseKeyTest {

    @Test
    void sameTenantAndCaseNumberShouldBeEqual() {
        ComplianceCaseKey first =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1001"
                );

        ComplianceCaseKey second =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1001"
                );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentTenantShouldCreateDifferentIdentity() {
        ComplianceCaseKey first =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1001"
                );

        ComplianceCaseKey second =
                new ComplianceCaseKey(
                        "TENANT-B",
                        "CASE-1001"
                );

        assertNotEquals(first, second);
    }

    @Test
    void differentCaseNumberShouldCreateDifferentIdentity() {
        ComplianceCaseKey first =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1001"
                );

        ComplianceCaseKey second =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1002"
                );

        assertNotEquals(first, second);
    }

    @Test
    void equivalentCompositeKeyShouldWorkWithHashMap() {
        ComplianceCaseKey storedKey =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1001"
                );

        ComplianceCaseKey lookupKey =
                new ComplianceCaseKey(
                        "TENANT-A",
                        "CASE-1001"
                );

        Map<ComplianceCaseKey, String> cases = new HashMap<>();

        cases.put(storedKey, "MANUAL_REVIEW");

        assertEquals("MANUAL_REVIEW", cases.get(lookupKey));
    }
}