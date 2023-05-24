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
                if(buffer.contains("JOBN") || buffer.contains("JOBP")) {
                    parseJobInfo(buffer.get());
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
                    if(!fitnessTest(serverToUse)) {
                        serverToUse = parseServerInfo(buffer.get());
                    }
                    buffer.update();
                }
                if(!fitnessTest(serverToUse)) {
                    serverToUse = parseServerInfo(buffer.get());
                }
            }
            return serverToUse;
        } catch (Exception e) {System.out.println("Error @ GETS Capable");}
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
                }
                else {
                    serverToUse = getsCapable();
                    if(!fitnessTest(serverToUse)) {
                        msg.send("OK");
                        queueJob();
                        serverToUse = new Server();
                    }
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

    public boolean fitnessTest(Server server) {
        return server.getCores() >= Integer.parseInt(jobInfo.get("cores"));
    }

    public int parseLSTJ(String msg) {
        String reg = "(([^ ]+)[ ])+(?<cores>[0-9])[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            return Integer.parseInt(m.group("cores"));
        }
        return 0;
    }

    public void queueJob() {
        try{
            msg.send("ENQJ GQ " + jobInfo.get("id"));
        } catch (Exception e) {System.out.println("Error @ queueJob()");}

    }

    public void dequeueFirst() {
        try {
            msg.send("DEQJ GQ 0");
        } catch (Exception e) {System.out.println("Error @ queueJob()");}
    }
}
