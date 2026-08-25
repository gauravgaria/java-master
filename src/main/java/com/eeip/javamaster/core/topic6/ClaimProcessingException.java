package com.eeip.javamaster.core.topic6;

/** Application-level failure raised when claim processing infrastructure fails. */
public final class ClaimProcessingException extends RuntimeException {

    public ClaimProcessingException(String message) {
        super(message);
    }

    public ClaimProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

