public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    String maxLargestServer;

    Scheduler(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
        this.largestServerName = "";
        this.maxLargestServer = "0";
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
                largestServerName = Parser.findLargestServer(buffer.get());
                maxLargestServer = Parser.getMaxNumServers(buffer.get());
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
}
