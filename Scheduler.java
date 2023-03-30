import java.util.ArrayList; 
import java.util.*;

public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    String maxLargestServer;
    ArrayList<ArrayList<String>> servers;
    ArrayList<Server> serversForJob;
    private Server server;

    Scheduler(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
        this.servers = new ArrayList<ArrayList<String>>();
        this.serversForJob = new ArrayList<Server>();
        this.server = new Server();
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

                chooseServer();
                for(Server server : serversForJob) {
                    System.out.println(server.getAll());
                }
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
    
    public void findLargestServers() {
    	    int maxCore = 0;
    	    Collections.reverse(servers);
    	    for(ArrayList<String> s : servers) {
    		if(Integer.parseInt(s.get(2)) >= maxCore) {
                maxCore = Integer.parseInt(s.get(2));
                server.setName(s.get(0));
                server.setId(s.get(1));
                server.setCores(s.get(2));
                serversForJob.add(server);
		    }
        }
    }

    public void chooseServer() {
        largestServerName = serversForJob.get(serversForJob.size()-1).getName();
        for(Server server : serversForJob) {
            if(server.getName().equals(largestServerName)) {
                maxLargestServer = server.getId();
            }
        }
    }
}
