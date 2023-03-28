public class ATL extends Scheduler { 

    ATL(Buffer buffer, Message msg, String largestServerName, Parser parser) {
        super(buffer, msg, largestServerName, parser);
    }

    @Override
    public void execute() {
        try {
            while(!buffer.get().contains("NONE")) {
                if(buffer.get().contains("JCPL")) {
                    msg.send("REDY");
                }
                else {
                    String jobToSchedule = "SCHD" + parser.getJobId(buffer.get()) + " " + largestServerName + " 0";
                    msg.send(jobToSchedule);
                    msg.send("REDY");
                }
            }

        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
    
}
