import java.util.Optional;
import java.net.*;
import java.io.*;

public class Client {
    private String ip;
    private int port;
    private Socket client;
    private BufferedReader input;
    private DataOutputStream output;
    private Buffer buffer;
    private Message msg;
    private AlgorithmFactory fact;
    private Optional<Scheduler> schedule;
    private String alg;
    
    public Client(String ip, int port, String alg) {
        this.ip = ip;
        this.port = port;
        this.alg = alg;
        initalise();
    }

    private void initalise() {
        try {
            client = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new DataOutputStream(client.getOutputStream());
            buffer = new Buffer(input);
            msg = new Message(output, buffer);
            fact = new AlgorithmFactory(buffer, msg);
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void execute() {
        auth();
        run();
        if(!client.isClosed()) {
            close();
        }
    }

    public void auth() {
        try {
            String[] msgs = {"HELO", "AUTH user", "REDY"};
            for(int i = 0; i < msgs.length; i++) {
                msg.send(msgs[i]);
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void run() {
        schedule = fact.getAlgorithm(alg);
        if(schedule.isPresent()) {
            schedule.get().getLargestServer();
            schedule.get().execute();
        }
        else {
            schedule = Optional.of(new LRR(buffer, msg));
            schedule.get().execute();
        }
    }

    public void close() {
        try {
            msg.send("QUIT");
            if(buffer.equals("QUIT")) {
                client.close();
            }
        } catch (Exception e) {System.out.println("COULD NOT CLOSE CLIENT GRACEFULLY");}

    }
}
