import java.util.Optional;

public class SchedulerFactory {
    private Buffer buffer;
    private Message msg;
    private Parser parser;

    SchedulerFactory(Buffer buffer, Message msg, Parser parser) {
        this.buffer = buffer;
        this.msg = msg;
        this.parser = parser;
    }
    
    public Optional<Scheduler> getAlgorithm(String algo) {
        switch(algo) {
            case "ATL":
                return Optional.of(new ATL(buffer, msg, parser));
            case "LRR":
                return Optional.of(new LazyGetsCapable(buffer, msg, parser));
            default:
                return Optional.empty();
        }
    }
}
