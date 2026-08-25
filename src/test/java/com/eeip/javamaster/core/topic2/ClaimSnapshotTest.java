package com.eeip.javamaster.core.topic2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClaimSnapshotTest {

    private static final Instant CAPTURED_AT =
            Instant.parse("2026-08-16T10:00:00Z");

    @Test
    void constructorMustDefensivelyCopyInputCollections() {
        List<ClaimLine> lines = new ArrayList<>();
        lines.add(new ClaimLine(
                "PROC-001",
                new BigDecimal("1500.00")
        ));

        List<String> diagnosisCodes =
                new ArrayList<>(List.of("DIA-001"));

        Map<String, String> metadata =
                new HashMap<>();

        metadata.put("source", "CLAIMS_SYSTEM");

        ClaimSnapshot snapshot = new ClaimSnapshot(
                "CLM-2026-0001",
                lines,
                diagnosisCodes,
                metadata,
                CAPTURED_AT
        );

        lines.add(new ClaimLine(
                "PROC-002",
                new BigDecimal("500.00")
        ));

        diagnosisCodes.add("DIA-002");
        metadata.put("risk", "HIGH");

        assertEquals(1, snapshot.claimLines().size());
        assertEquals(1, snapshot.diagnosisCodes().size());
        assertEquals(
                Map.of("source", "CLAIMS_SYSTEM"),
                snapshot.metadata()
        );
    }

    @Test
    void returnedCollectionsMustNotBeModifiable() {
        ClaimSnapshot snapshot = createSnapshot();

        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.claimLines().add(
                        new ClaimLine(
                                "PROC-002",
                                new BigDecimal("500")
                        )
                )
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.diagnosisCodes().add("DIA-002")
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.metadata().put(
                        "risk",
                        "HIGH"
                )
        );
    }

    @Test
    void claimLinesShouldBeDeeplyImmutable() {
        ClaimLine line = new ClaimLine(
                "PROC-001",
                new BigDecimal("1500.00")
        );

        ClaimSnapshot snapshot = new ClaimSnapshot(
                "CLM-2026-0001",
                List.of(line),
                List.of("DIA-001"),
                Map.of("source", "CLAIMS_SYSTEM"),
                CAPTURED_AT
        );

        assertEquals(
                "PROC-001",
                snapshot.claimLines()
                        .getFirst()
                        .procedureCode()
        );

        assertEquals(
                new BigDecimal("1500.00"),
                snapshot.claimLines()
                        .getFirst()
                        .amount()
        );
    }

    @Test
    void nullCollectionsMustBeRejected() {
        assertThrows(
                NullPointerException.class,
                () -> new ClaimSnapshot(
                        "CLM-2026-0001",
                        null,
                        List.of("DIA-001"),
                        Map.of(),
                        CAPTURED_AT
                )
        );
    }

    private ClaimSnapshot createSnapshot() {
        return new ClaimSnapshot(
                "CLM-2026-0001",
                List.of(
                        new ClaimLine(
                                "PROC-001",
                                new BigDecimal("1500")
                        )
                ),
                List.of("DIA-001"),
                Map.of("source", "CLAIMS_SYSTEM"),
                CAPTURED_AT
        );
    }
}