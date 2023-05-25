import java.net.*;
import java.io.*;

public class Client {
    private String ip;
    private int port;
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;
    private Buffer buffer;
    private Message msg;
    private SchedulerFactory fact;
    private Scheduler schedule;
    private String alg;

    public Client(String ip, int port, String alg) {
        this.ip = ip;
        this.port = port;
        this.alg = alg;
        initalise();
    }

    private void initalise() {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            buffer = new Buffer(input);
            msg = new Message(output, buffer);
            fact = new SchedulerFactory(buffer, msg);
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void execute() {
        auth();
        run();
        if(!socket.isClosed()) {
            close();
        }
    }

    public void auth() {
        try {
            String[] msgs = {"HELO", "AUTH " + System.getProperty("user.name"), "REDY"};
            for(int i = 0; i < msgs.length; i++) {
                msg.send(msgs[i]);
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void run() {
        schedule = fact.getAlgorithm(alg);
        schedule.execute();
    }

    public void close() {
        try {
            msg.send("QUIT");
            if(buffer.equals("QUIT")) {
                socket.close();
            }
        } catch (Exception e) {System.out.println("COULD NOT CLOSE CLIENT GRACEFULLY");}

    }
}
