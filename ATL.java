public class ATL extends Scheduler { 

    ATL(Buffer buffer, Message msg, Parser parser) {
        super(buffer, msg, parser);
    }

    @Override
    public void execute() {
        try {
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JCPL")) {
                    msg.send("REDY");
                }
                else {
                    String jobToSchedule = "SCHD" + getJobId(buffer.get()) + " " + largestServerName + " 0";
                    msg.send(jobToSchedule);
                    msg.send("REDY");
                }
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
}
