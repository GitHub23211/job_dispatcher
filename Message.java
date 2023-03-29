import java.io.DataOutputStream;

public class Message {
    private DataOutputStream output;
    private Buffer buffer;

    Message(DataOutputStream output, Buffer buffer) {
        this.output = output;
        this.buffer = buffer;
    }

    public void send(String msg) {
        try {
            output.write((msg + "\n").getBytes());
            output.flush();
            buffer.update();
        } catch (Exception e) {System.out.println("Error @ messages: " + e);}
    }    
}
