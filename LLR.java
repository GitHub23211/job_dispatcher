public class LLR extends Scheduler{


    LLR(Buffer buffer, Message msg, Parser parser) {
        super(buffer, msg, parser);
    }

    @Override
    public void execute() {
        try {
            int i = 0;
            while(!buffer.get().contains("NONE")) {
                if(buffer.get().contains("JCPL")) {
                    msg.send("REDY");
                }
                else {
                    String jobToSchedule = "SCHD" + parser.getJobId(buffer.get()) + " " + largestServerName + " " + i;
                    msg.send(jobToSchedule);
                    msg.send("REDY");
                    i++;
                }
                if(i > Integer.parseInt(maxLargestServer)) {
                    i = 0;
                }
            }

        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
}