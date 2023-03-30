public class LRR extends Scheduler{

    LRR(Buffer buffer, Message msg, Parser parser) {
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
                    msg.send(getJob() + i);
                    msg.send("REDY");
                    i++;
                }
                if(i > maxLargestServer) {
                    i = 0;
                }
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
}