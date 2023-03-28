import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Client {
    private String ip;
    private int port;
    private Socket client;
    private BufferedReader input;
    private DataOutputStream output;
    private String buffer;
    private String largestServerName;
    private int largestServerMax;
    
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.buffer = "";
        initalise();
    }

    private void initalise() {
        try {
            client = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void auth() {
        try {
            String[] msgs = {"HELO", "AUTH user", "REDY"};
            for(int i = 0; i < msgs.length; i++) {
                sendMsg(msgs[i]);
                printMsg();
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void getServerInfo() {
        try {
            sendMsg("GETS All");
            printMsg();
            if(buffer.contains("DATA")) {
                sendMsg("OK");
                buffer = input.readLine();
                while(input.ready()) {
                    buffer = input.readLine();
                }
                getLargestServer(buffer);
                sendMsg("OK");
            }
            printMsg();
            sendMsg("REDY");
            printMsg();
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }

    public void scheduleJobs() {
        try {
            int i = 0;
            while(!buffer.contains("NONE")) {
                if(buffer.contains("JCPL")) {
                    sendMsg("REDY");
                    printMsg();
                }
                else {
                    String jobToSchedule = "SCHD" + getJobId(buffer) + " " + largestServerName + " " + i;
                    sendMsg(jobToSchedule);
                    printMsg();
                    sendMsg("REDY");
                    printMsg();
                    i++;
                }
                if(i > largestServerMax) {
                    i = 0;
                }
            }

        } catch (Exception e) {System.out.println("Error @ job scheduling: " + e);}
    }

    private void sendMsg(String msg) {
        try {
            output.write((msg + "\n").getBytes());
            output.flush();
        } catch (Exception e) {System.out.println("Error @ sending message to server: " + e);}
    }

    private void printMsg() {
        try {
            buffer = input.readLine();
        } catch (Exception e) {System.out.println("Error @ printing server messages: " + e);}
    }       

    private void getLargestServer(String msg) throws Exception {
        Pattern reg = Pattern.compile("([^ ]+)([ ])([0-9]+)");
        Matcher m = reg.matcher(msg);
        if(m.find()) {
            largestServerName = m.group(1);
            largestServerMax = Integer.parseInt(m.group(3));
        }
        else {
            throw new Exception("Could not find largest server ID");
        }
    }

    private String getJobId(String msg) throws Exception {
        Pattern reg = Pattern.compile("([a-zA-Z ])+[0-9]+([ ]+[0-9]+)");
        Matcher m = reg.matcher(msg);
        if(m.find()) {
            if(m.group(0).contains("JOBN")){
                return m.group(2);
            }
            return "0";
        }
        throw new Exception("Could not get Job Id");
    }

    public void close() {
        try {
            sendMsg("QUIT");
            client.close();
        } catch (Exception e) {System.out.println("COULD NOT CLOSE CLIENT GRACEFULLY");}

    }
}
