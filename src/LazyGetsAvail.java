import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public class LazyGetsAvail extends Scheduler {
    String jobId;
    HashMap<String, String> serverInfo;
    HashMap<String, String> jobInfo;

    LazyGetsAvail(Buffer buffer, Message msg, Parser parser) {
        super(buffer, msg, parser);
        serverInfo = new HashMap<String, String>();
        jobInfo = new HashMap<String, String>();
    }

    @Override
    public void execute() {
        try {
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JOBN")) {
                    parseJobInfo(buffer.get());
                    getsAvail();
                    msg.send(scheduleJob());
                }   
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }

    public String scheduleJob() {
        return "SCHD " + jobInfo.get("id") + " " + serverInfo.get("name") + " " + serverInfo.get("id");
    }

    public void getsCapable() {
        msg.send("GETS Capable " + jobInfo.get("cores") + " " + jobInfo.get("memory") + " " + jobInfo.get("disk"));
        System.out.println(buffer.get());
        if(buffer.contains("DATA")) {
            msg.send("OK");
            parseServerInfo(buffer.get());
            while(buffer.isReady()){
                buffer.update();
            } 
            msg.send("OK");
        }
    }

    public void getsAvail() {
        try {
            msg.send("GETS Avail " + jobInfo.get("cores") + " " + jobInfo.get("memory") + " " + jobInfo.get("disk"));
            if(buffer.contains("DATA")) {
                msg.send("OK");
                if(!buffer.get().contains(".")) {
                    parseServerInfo(buffer.get());
                    while(buffer.isReady()) {
                        buffer.update();
                    }
                    msg.send("OK");
                }
                else {
                    getsCapable();
                }
            }
        } catch (Exception e) {System.out.println("Error @ GETS Avail");}
    }

    public void parseServerInfo(String msg) {     
        String reg = "(?<name>[^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            serverInfo.put("name", m.group("name"));
            serverInfo.put("id", m.group("id"));
            serverInfo.put("cores", m.group("cores"));
            serverInfo.put("memory", m.group("memory"));
            serverInfo.put("disk", m.group("disk"));
        }
    }

    public void parseJobInfo(String msg) {
        String reg = "(?<name>[^ ]+)[ ]([^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            jobInfo.put("id", m.group("id"));
            jobInfo.put("cores", m.group("cores"));
            jobInfo.put("memory", m.group("memory"));
            jobInfo.put("disk", m.group("disk"));
        }
    }
}
