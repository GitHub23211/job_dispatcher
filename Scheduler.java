public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    Parser parser;

    Scheduler(Buffer buffer, Message msg, String largestServerName, Parser parser) {
        this.buffer = buffer;
        this.msg = msg;
        this.largestServerName = largestServerName;
        this.parser = parser;
    }

    public abstract void execute();

    public void getLargestServer() {
        try {
            msg.send("GETS All");
            if(buffer.contains("DATA")) {
                msg.send("OK");
                while(buffer.isReady()) {
                    buffer.update();
                }
                parser.findLargestServer(buffer.get());
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
}
