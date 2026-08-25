package com.eeip.javamaster.core.topic6;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClaimProcessingServiceTest {

    private static final String CLAIM_NUMBER = "CLM-2026-0001";
    private static final BigDecimal CLAIM_AMOUNT = new BigDecimal("1250.00");

    @Test
    void successfulClaimProcessingReturnsClaim() {
        Claim claim = new Claim(CLAIM_NUMBER, CLAIM_AMOUNT);
        ClaimProcessingService service = serviceUsing(resourceReturningClaim(claim));

        Optional<Claim> result = service.process(CLAIM_NUMBER);

        assertEquals(Optional.of(claim), result);
    }

    @Test
    void missingClaimIsNormalEmptyOutcome() {
        ClaimProcessingService service = serviceUsing(resourceNotFound());

        Optional<Claim> result = service.process(CLAIM_NUMBER);

        assertFalse(result.isPresent());
    }

    @Test
    void ioFailureIsTranslatedToClaimProcessingException() {
        ClaimProcessingException exception = assertThrows(
                ClaimProcessingException.class,
                () -> serviceUsing(resourceFailingWith(new IOException("resource unavailable")))
                        .process(CLAIM_NUMBER)
        );

        assertInstanceOf(ClaimProcessingException.class, exception);
    }

    @Test
    void translatedExceptionPreservesOriginalIoCause() {
        IOException ioFailure = new IOException("resource unavailable");

        ClaimProcessingException exception = assertThrows(
                ClaimProcessingException.class,
                () -> serviceUsing(resourceFailingWith(ioFailure)).process(CLAIM_NUMBER)
        );

        assertInstanceOf(IOException.class, exception.getCause());
        assertEquals(ioFailure, exception.getCause());
    }

    @Test
    void translatedMessageContainsContextWithoutSensitiveAmount() {
        ClaimProcessingException exception = assertThrows(
                ClaimProcessingException.class,
                () -> serviceUsing(resourceFailingWith(new IOException("unavailable")))
                        .process(CLAIM_NUMBER)
        );

        assertTrue(exception.getMessage().contains(CLAIM_NUMBER));
        assertFalse(exception.getMessage().contains(CLAIM_AMOUNT.toString()));
    }

    @Test
    void tryWithResourcesClosesResourceAfterSuccess() {
        AtomicBoolean closed = new AtomicBoolean();
        ClaimProcessingService service = serviceUsing(resourceWithSuccessfulCloseFlag(closed));

        service.process(CLAIM_NUMBER);

        assertTrue(closed.get());
    }

    @Test
    void tryWithResourcesClosesResourceAfterFailure() {
        AtomicBoolean closed = new AtomicBoolean();
        IOException failure = new IOException("read failed");
        ClaimProcessingService service = serviceUsing(resourceWithFailedCloseFlag(closed, failure));

        assertThrows(ClaimProcessingException.class, () -> service.process(CLAIM_NUMBER));
        assertTrue(closed.get());
    }

    private ClaimProcessingService serviceUsing(ClaimDataReader.ClaimResource resource) {
        return new ClaimProcessingService(new ClaimDataReader(() -> resource));
    }

    private ClaimDataReader.ClaimResource resourceReturningClaim(Claim claim) {
        return claimNumber -> Optional.of(claim);
    }

    private ClaimDataReader.ClaimResource resourceNotFound() {
        return claimNumber -> Optional.empty();
    }

    private ClaimDataReader.ClaimResource resourceFailingWith(IOException failure) {
        return claimNumber -> {
            throw failure;
        };
    }

    private ClaimDataReader.ClaimResource resourceWithSuccessfulCloseFlag(AtomicBoolean closed) {
        return new ClaimDataReader.ClaimResource() {
            @Override
            public Optional<Claim> find(String claimNumber) {
                return Optional.empty();
            }

            @Override
            public void close() {
                closed.set(true);
            }
        };
    }

    private ClaimDataReader.ClaimResource resourceWithFailedCloseFlag(
            AtomicBoolean closed,
            IOException failure
    ) {
        return new ClaimDataReader.ClaimResource() {
            @Override
            public Optional<Claim> find(String claimNumber) throws IOException {
                throw failure;
            }

            @Override
            public void close() {
                closed.set(true);
            }
        };
    }
}

