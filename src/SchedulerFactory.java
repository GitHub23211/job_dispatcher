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
            case "lga":
                return Optional.of(new LazyGetsAvail(buffer, msg, parser));
            case "bga":
                return Optional.of(new GetsAvail(buffer, msg, parser));
            default:
                return Optional.empty();
        }
    }
}
