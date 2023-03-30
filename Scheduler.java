import java.util.ArrayList; 
import java.util.*;

public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    String maxLargestServer;
    ArrayList<ArrayList<String>> servers;
    ArrayList<String> serversForJob;

    Scheduler(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
        this.servers = new ArrayList<ArrayList<String>>();
        this.serversForJob = new ArrayList<String>();
        this.largestServerName = "";
        this.maxLargestServer = "0";
    }

    public abstract void execute();

    public void getLargestServer() {
        try {
            msg.send("GETS All");
            if(buffer.contains("DATA")) {
                msg.send("OK");
                while(buffer.isReady()) {
                    buffer.update();
                    servers.add(Parser.getServerInfo(buffer.get()));
                }
                
                findLargestServers();
                System.out.println(serversForJob);
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
    
    public void findLargestServers() {
    	    int maxCore = 0;
    	    Collections.reverse(servers);
    	    for(ArrayList<String> server : servers) {
    		if(Integer.parseInt(server.get(2)) >= maxCore) {
			maxCore = Integer.parseInt(server.get(2));
			serversForJob.add(server.get(0) + server.get(1) + server.get(2));
		}
    	    }
    }
}
