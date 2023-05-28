public abstract class Scheduler {
    protected Buffer buffer;
    protected Message msg;
    protected Job job;

    Scheduler(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
        this.job = new Job();
    }

    public abstract void execute();

    public Server getsCapable() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Capable " + job.jobInfo());
            if(buffer.contains("DATA")) {
                msg.send("OK");
                Server temp = Parser.parseServerInfo(buffer.get());
                while(buffer.isReady()){
                    if(job.coreFit(temp)) {
                        serverToUse = temp;
                    }
                    buffer.update();
                    temp = Parser.parseServerInfo(buffer.get());
                }
                if(job.coreFit(temp)) {
                    serverToUse = temp;
                }
                if(!fitnessTest(serverToUse)) {
                    serverToUse = new Server();
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
        boolean matchCores = server.cores >= job.cores;
        boolean matchMem = server.mem >= job.mem;
        boolean matchDisk = server.disk >= job.disk;
        return (matchCores && matchMem && matchDisk);
    }


    public String scheduleJob(Server serverToUse) {
        return "SCHD " + job.id + " " + serverToUse.getNameAndID();
    }

    public void queueJob() {
        try{
            msg.send("ENQJ GQ");
        } catch (Exception e) {System.out.println("Error @ queueJob()"); e.printStackTrace();}

    }
    
    public void dequeueFirst() {
        try {
            msg.send("DEQJ GQ 0");
        } catch (Exception e) {System.out.println("Error @ queueJob()"); e.printStackTrace();}
    }
}
