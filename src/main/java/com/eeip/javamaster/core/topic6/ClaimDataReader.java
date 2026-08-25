package com.eeip.javamaster.core.topic6;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** Reads claim data from a resource-like source and translates I/O failures. */
public final class ClaimDataReader {

    private final Supplier<? extends ClaimResource> resourceSupplier;

    public ClaimDataReader(Supplier<? extends ClaimResource> resourceSupplier) {
        this.resourceSupplier = Objects.requireNonNull(
                resourceSupplier,
                "resourceSupplier must not be null"
        );
    }

    public Optional<Claim> find(String claimNumber) {
        try (ClaimResource resource = resourceSupplier.get()) {
            return resource.find(claimNumber);
        } catch (IOException exception) {
            throw new ClaimProcessingException(
                    "Unable to read claim data for claimNumber=" + claimNumber,
                    exception
            );
        }
    }

    /** Small resource abstraction used to make cleanup observable in tests. */
    @FunctionalInterface
    public interface ClaimResource extends AutoCloseable {

        Optional<Claim> find(String claimNumber) throws IOException;

        @Override
        default void close() throws IOException {
            // A real resource overrides this; the default keeps simple test resources small.
        }
    }
}


