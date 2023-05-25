public class GetsAvailQ extends Scheduler {

    GetsAvailQ(Buffer buffer, Message msg) {
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
                    msg.send("LSTQ GQ #");
                    if(Integer.parseInt(buffer.get()) > 0) {
                        dequeueFirst();
                    }
                }
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
}
