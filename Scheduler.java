public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    String maxLargestServer;
    Parser parser;

    Scheduler(Buffer buffer, Message msg, Parser parser) {
        this.buffer = buffer;
        this.msg = msg;
        this.largestServerName = "";
        this.maxLargestServer = "0";
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
                largestServerName = parser.findLargestServer(buffer.get());
                maxLargestServer = parser.getMaxNumServers(buffer.get());
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
}
