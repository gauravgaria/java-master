package com.eeip.javamaster.core.topic6;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Coordinates claim lookup while keeping business absence separate from infrastructure failure. */
public final class ClaimProcessingService {

    private static final Logger LOGGER = Logger.getLogger(ClaimProcessingService.class.getName());

    private final ClaimDataReader dataReader;

    public ClaimProcessingService(ClaimDataReader dataReader) {
        this.dataReader = dataReader;
    }

    public Optional<Claim> process(String claimNumber) {
        try {
            return dataReader.find(claimNumber);
        } catch (ClaimProcessingException exception) {
            // Log the safe identifier and exception object; do not stringify a stack trace or claim data.
            LOGGER.log(
                    Level.SEVERE,
                    "Claim processing failed claimNumber=" + claimNumber,
                    exception
            );
            throw exception;
        }
    }

    /*
     * Bad: catch (Exception e) { } — swallowing the failure hides an operational problem.
     * Bad: throw new ClaimProcessingException("failed") — this discards the original cause.
     * Good: the reader translates IOException with ClaimProcessingException(message, cause).
     * Catching Throwable is also inappropriate here because it includes serious JVM errors.
     */
}

