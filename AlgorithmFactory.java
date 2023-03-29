import java.util.Optional;

public class AlgorithmFactory {
    private Buffer buffer;
    private Message msg;

    AlgorithmFactory(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
    }
    
    public Optional<Scheduler> getAlgorithm(String algo) {
        switch(algo) {
            case "ATL":
                return Optional.of(new ATL(buffer, msg));
            case "LRR":
                return Optional.of(new LRR(buffer, msg));
            default:
                return Optional.empty();
        }
    }
}
