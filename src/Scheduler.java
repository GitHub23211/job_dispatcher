public abstract class Scheduler {
    protected Buffer buffer;
    protected Message msg;
    protected Job job;
    protected String largestServerName;
    protected int maxLargestServer;

    Scheduler(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
        this.job = new Job();
        this.largestServerName = "";
        this.maxLargestServer = 0;
    }

    public abstract void execute();

    public Server getsCapable() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Capable " + job.jobInfo());
            if(buffer.contains("DATA")) {
                msg.send("OK");
                serverToUse = Parser.parseServerInfo(buffer.get());
                while(buffer.isReady()){
                    if(!fitnessTest(serverToUse)) {
                        serverToUse = Parser.parseServerInfo(buffer.get());
                    }
                    buffer.update();
                }
                if(!fitnessTest(serverToUse)) {
                    serverToUse = Parser.parseServerInfo(buffer.get());
                }
                msg.send("OK");
            }
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Capable"); e.printStackTrace();}
        return serverToUse;
    }

    public Server getsAvail() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Avail " + job.jobInfo());
            if(buffer.contains("DATA")) {
                msg.send("OK");
                if(!(buffer.get().length() <= 1 || buffer.equals("."))) {
                    serverToUse = Parser.parseServerInfo(buffer.get());
                    while(buffer.isReady()) {
                        buffer.update();
                    }
                    msg.send("OK");
                }
            }
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Avail"); e.printStackTrace();}
        return serverToUse;
    }

    public boolean fitnessTest(Server server) {
        return server.getCores() >= job.cores;
    }

    public String scheduleJob(Server serverToUse) {
        return "SCHD " + job.id + " " + serverToUse.getName() + " " + serverToUse.getId();
    }

    public void queueJob() {
        try{
            msg.send("ENQJ GQ " + job.id);
        } catch (Exception e) {System.out.println("Error @ queueJob()"); e.printStackTrace();}

    }
    
    public void dequeueFirst() {
        try {
            msg.send("DEQJ GQ 0");
        } catch (Exception e) {System.out.println("Error @ queueJob()"); e.printStackTrace();}
    }
}
