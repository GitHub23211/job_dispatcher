public class GetsAvailBestQ extends Scheduler {

    GetsAvailBestQ(Buffer buffer, Message msg) {
        super(buffer, msg);
    }

    @Override
    public void execute() {
        try {
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JOBN") || buffer.contains("JOBP")) {
                    job = Parser.parseJobInfo(buffer.get());
                    Server serverToUse = getsAvail();
                    if(!serverToUse.getName().contains("error")) {
                        msg.send("OK");
                        msg.send(scheduleJob(serverToUse));
                    }
                }
                if(buffer.contains("JCPL")) {
                    String[] arr = Parser.parseJCPL(buffer.get());
                    Server serverToUse = getsType(arr[0], arr[1]);
                    msg.send("OK");
                    msg.send("LSTQ GQ #");
                    if(Integer.parseInt(buffer.get()) > 0) {
                        String jobIndex = listGQ(serverToUse);
                        msg.send("OK");
                        dequeueAtIndex(jobIndex);
                    }
                }
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
    
    public Server getsType(String server, String id) {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Type " + server);
            if(buffer.contains("DATA")) {
                msg.send("OK");
                if(!(buffer.get().length() <= 1 || buffer.equals("."))) {
                    serverToUse = Parser.parseServerInfo(buffer.get());
                    while(buffer.isReady()) {
                        if(!serverToUse.getId().equals(id)) {
                            serverToUse = Parser.parseServerInfo(buffer.get());
                        }
                        buffer.update();
                    }
                }
                if(!serverToUse.getId().equals(id)) {
                    serverToUse = Parser.parseServerInfo(buffer.get());
                }
            }
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Type");}
        return serverToUse;
    }

    public String listGQ(Server server) {
        int jobIndex = 0;
        int savedIndex = 0;
        try{
            msg.send("LSTQ GQ *");
            if(buffer.contains("DATA")) {
                msg.send("OK");
                if(!(buffer.get().length() <= 1 || buffer.equals("."))) {
                    job = Parser.parseJobinQ(buffer.get());
                    while(buffer.isReady()) {
                        if(job.cores <= server.getCores()) {
                            job = Parser.parseJobinQ(buffer.get());
                            savedIndex = jobIndex;
                        }
                        jobIndex++;
                        buffer.update();
                    }
                }
                if(job.cores <= server.getCores()) {
                    job = Parser.parseJobinQ(buffer.get());
                    savedIndex = jobIndex;
                }
                return String.valueOf(savedIndex);
            }
        } catch (Exception e) {System.out.println("Error @ listGQ() ");e.printStackTrace();}
        return String.valueOf(jobIndex);
    }
}
