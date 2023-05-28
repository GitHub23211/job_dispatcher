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
        boolean booting = checkBooting(server);
        return matchCores && matchMem && matchDisk && booting;
    }

    public boolean checkBooting(Server server) {
        if(server.state.contains("booting")) {
            if(server.cores >= job.cores) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    public String scheduleJob(Server serverToUse) {
        return "SCHD " + job.id + " " + serverToUse.name + " " + serverToUse.id;
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
