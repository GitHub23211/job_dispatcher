import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FC extends Scheduler{

    String serverName;
    String jobId;
    
    FC(Buffer buffer, Message msg, Parser parser) {
        super(buffer, msg, parser);
        this.serverName = "";
        this.jobId = "";
    }

    @Override
    public void execute() {
        try {
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JOBN")) {
              capableParse(); 
                    msg.send(getJob1());
                }   
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }
    
    public String getJob1() {
        return "SCHD " + this.jobId + " " + serverName + " 0";
    }
    
    public String[] getJobInfo(String msg) {
        String reg = "([^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ]([^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
        	String[] arr = {m.group(5), m.group(6), m.group(7)};
        	this.jobId = m.group(3);
            return arr;
        }
        String[] err = {"error"};
        return err;
    }
    
    public void capableParse() {
        try {
            String[] info = getJobInfo(buffer.get());
            msg.send("GETS Capable " + info[0] + " " + info[1] + " " + info[2]);
            if(buffer.contains("DATA")) {
                msg.send("OK");
                serverName = getLargestServer(buffer.get());
                while(buffer.isReady()) {
                    buffer.update();
                }
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
    
   private String getLargestServer(String msg) {
        String reg = "(?<name>[^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
           return m.group("name");
        }
        return "error";
    }
    
}
