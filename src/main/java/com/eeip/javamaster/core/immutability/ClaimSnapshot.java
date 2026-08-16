import java.util.List;
import java.util.Map;

public final class ClaimSnapshot {

    private final String claimNumber;
    private final List<String> diagnosisCodes;
    private final Map<String, String> metadata;

    public ClaimSnapshot(
            String claimNumber,
            List<String> diagnosisCodes,
            Map<String, String> metadata
    ) {
        this.claimNumber = claimNumber;
        this.diagnosisCodes = diagnosisCodes;
        this.metadata = metadata;
    }

    public List<String> diagnosisCodes() {
        return diagnosisCodes;
    }

    public Map<String, String> metadata() {
        return metadata;
    }
}