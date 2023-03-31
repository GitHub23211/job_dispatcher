import java.util.Optional;

public class ParserFactory {
    private Buffer buffer;
    private Message msg;

    ParserFactory(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
    }
    
    public Optional<Parser> getParser(String algo) {
        switch(algo) {
            case "XML":
                return Optional.of(new XMLMain());
            case "GETS":
                return Optional.of(new GetsParser(buffer, msg));
            default:
                return Optional.empty();
        }
    }
}
