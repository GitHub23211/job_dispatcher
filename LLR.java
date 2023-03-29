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





    // public void LRR() {
    //     try {
    //         int i = 0;
    //         while(!buffer.contains("NONE")) {
    //             if(buffer.contains("JCPL")) {
    //                 sendMsg("REDY");
    //             }
    //             else {
    //                 String jobToSchedule = "SCHD" + getJobId(buffer) + " " + largestServerName + " " + i;
    //                 sendMsg(jobToSchedule);
    //                 sendMsg("REDY");
    //                 i++;
    //             }
    //             if(i > largestServerMax) {
    //                 i = 0;
    //             }
    //         }N

    //     } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    // }

    // public void ATL() {
    //     try {
    //         while(!buffer.contains("NONE")) {
    //             if(buffer.contains("JCPL")) {
    //                 sendMsg("REDY");
    //             }
    //             else {
    //                 String jobToSchedule = "SCHD" + getJobId(buffer) + " " + largestServerName + " 0";
    //                 sendMsg(jobToSchedule);
    //                 sendMsg("REDY");
    //             }
    //         }

    //     } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    // }