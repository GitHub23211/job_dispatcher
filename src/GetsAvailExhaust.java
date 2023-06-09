public class GetsAvailExhaust extends Scheduler{

    GetsAvailExhaust(Buffer buffer, Message msg) {
        super(buffer, msg);
    }
    
    @Override
    public void execute() {
        try {
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JOBN") || buffer.contains("JOBP")) {
                    job = Parser.parseJobInfo(buffer.get());
                    Server serverToUse = getsAvailExact();
                    if(!serverToUse.isValid()) {
                        serverToUse = getsAvail();
                        if(!serverToUse.isValid()) {
                            serverToUse = getsCapable();
                            if(!serverToUse.isValid()) {
                                queueJob();
                                serverToUse = new Server(); 
                            }
                        }
                    }
                    if(serverToUse.isValid()) {
                        msg.send(scheduleJob(serverToUse));
                    }
                }
                if(buffer.contains("JCPL") || buffer.contains("CHKQ")) {
                    msg.send("LSTQ GQ #");
                    if(Integer.parseInt(buffer.get()) > 0) {
                        dequeueFirst();
                    }
                }
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
    
    public Server getsAvailExact() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Avail " + job.jobInfo());
            if(buffer.contains("DATA")) {
                msg.send("OK");
                if(!(buffer.get().length() <= 1 || buffer.equals("."))) {
                    serverToUse = Parser.parseServerInfo(buffer.get());
                    while(buffer.isReady()) {
                        if(!exactFitnessTest(serverToUse)) {
                            serverToUse = Parser.parseServerInfo(buffer.get());
                        }
                        buffer.update();
                    }
                    if(!exactFitnessTest(serverToUse)) {
                        serverToUse = Parser.parseServerInfo(buffer.get());
                    }
                    if(!exactFitnessTest(serverToUse)) {
                        serverToUse = new Server();
                    }
                    msg.send("OK");
                }
            }
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Avail Exact"); e.printStackTrace();}
        return serverToUse;
    }

    public boolean exactFitnessTest(Server server) {
        boolean matchCores =  server.cores == job.cores;
        boolean matchMem = server.mem >= job.mem;
        boolean matchDisk = server.disk >= job.disk;
        return matchCores && matchMem && matchDisk;
    }
}
