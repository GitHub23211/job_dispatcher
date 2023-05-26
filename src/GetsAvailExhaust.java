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
                    if(serverToUse.getName().contains("error")) {
                        serverToUse = getsCapableExact();
                        if(serverToUse.getName().contains("error")) {
                            serverToUse = getsAvail();
                            if(serverToUse.getName().contains("error")) {
                                serverToUse = getsCapable();
                                if(!fitnessTest(serverToUse)) {
                                    queueJob();
                                    serverToUse = new Server();
                                }
                            }
                        }
                    }
                    if(!serverToUse.getName().contains("error")) {
                        msg.send(scheduleJob(serverToUse));
                    }
                }
                if(buffer.contains("JCPL")) {
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

    public Server getsCapableExact() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Capable " + job.jobInfo());
            if(buffer.contains("DATA")) {
                msg.send("OK");
                serverToUse = Parser.parseServerInfo(buffer.get());
                while(buffer.isReady()){
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
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Capable"); e.printStackTrace();}
        return serverToUse;
    }

    public boolean exactFitnessTest(Server server) {
        return server.getCores() == job.cores;
    }
}
