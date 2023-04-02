public class LRR extends Scheduler{

    LRR(Buffer buffer, Message msg, Parser parser) {
        super(buffer, msg, parser);
    }

    @Override
    public void execute() {
        try {
            int i = 0;
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JOBN")) {
                    msg.send(getJob() + i);
                    i++;
                }                
                if(i >= maxLargestServer) {
                    i = 0;
                }
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
}