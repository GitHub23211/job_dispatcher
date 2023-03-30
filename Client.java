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
    private ParserFactory parseFact;
    private Optional<Parser> parser;
    private Optional<Scheduler> schedule;
    private String alg;
    private String parse;

    public Client(String ip, int port, String alg, String parse) {
        this.ip = ip;
        this.port = port;
        this.alg = alg;
        this.parse = parse;
        initalise();
    }

    private void initalise() {
        try {
            client = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new DataOutputStream(client.getOutputStream());
            buffer = new Buffer(input);
            msg = new Message(output, buffer);
            parseFact = new ParserFactory(buffer, msg);
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
            String[] msgs = {"HELO", "AUTH js45203083", "REDY"};
            for(int i = 0; i < msgs.length; i++) {
                msg.send(msgs[i]);
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void run() {
        parser = parseFact.getParser(parse);
        fact = new AlgorithmFactory(buffer, msg, parser.get());
        
        schedule = fact.getAlgorithm(alg);
        if(schedule.isPresent()) {
            schedule.get().setServers();
            schedule.get().execute();
   	    }
        else {
            close();
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
