import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public class GetsAvail extends Scheduler {
    String jobId;
    HashMap<String, Server> servers;
    HashMap<String, String> jobInfo;

    GetsAvail(Buffer buffer, Message msg, Parser parser) {
        super(buffer, msg, parser);
        servers = new HashMap<String, Server>();
        jobInfo = new HashMap<String, String>();
    }

    @Override
    public void execute() {
        try {
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JOBN")) {
                    parseJobInfo(buffer.get());
                    msg.send(scheduleJob(getsAvail()));
                }   
                msg.send("REDY");
            }
        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }

    public String scheduleJob(Server serverToUse) {
        return "SCHD " + jobInfo.get("id") + " " + serverToUse.getName() + " " + serverToUse.getId();
    }

    public Server getsCapable() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Capable " + jobInfo.get("cores") + " " + jobInfo.get("memory") + " " + jobInfo.get("disk"));
            if(buffer.contains("DATA")) {
                msg.send("OK");
                serverToUse = parseServerInfo(buffer.get());
                while(buffer.isReady()){
                    // findBestCapable();
                    buffer.update();
                    System.out.println(" after buffer update " + buffer.get());
                    System.out.println(" after buffer update " + buffer.isReady());
                } 
            }
        } catch (Exception e) {System.out.println("Error @ GETS Avail");}
        return serverToUse;
    }

    public Server getsAvail() {
        Server serverToUse = new Server();
        try {
            msg.send("GETS Avail " + jobInfo.get("cores") + " " + jobInfo.get("memory") + " " + jobInfo.get("disk"));
            if(buffer.contains("DATA")) {
                msg.send("OK");
                if(!buffer.get().contains(".")) {
                    serverToUse = parseServerInfo(buffer.get());
                    while(buffer.isReady()) {
                        buffer.update();
                    }
                    msg.send("OK");
                }
                else {
                    serverToUse = getsCapable();
                    msg.send("OK");
                }
            }
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Avail");}
        return serverToUse;
    }

    public Server parseServerInfo(String msg) {     
        String reg = "(?<name>[^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            return new Server(m.group("name"), m.group("id"), Integer.parseInt(m.group("cores")));
            // servers.put(serv.getName()+serv.getId(), serv);
        }
        return new Server();
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

    // public boolean findBestCapable() {
    //     System.out.println(" start of func " + buffer.get());
    //     int serverCores = Integer.parseInt(serverInfo.get("cores"));
    //     int jobCores = Integer.parseInt(jobInfo.get("cores"));
    //     System.out.println(" before if " + buffer.get());
    //     if(serverCores >= jobCores) {
    //         msg.send("LSTJ " + serverInfo.get("name") + " " + serverInfo.get("id"));
    //         msg.send("OK");
    //         int coresInUse = parseLSTJ(buffer.get());
    //        return (serverCores - coresInUse >= jobCores);
    //     }
    //     return false;
    // }
}
