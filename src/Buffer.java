import java.io.BufferedReader;

public class Buffer {
    private String buffer;
    private BufferedReader input;

    Buffer(BufferedReader input) {
        this.input = input;
        this.buffer = "";
    }

    public String get() {
        return buffer;
    }

    public void update(){
        try {
            buffer = input.readLine();
        }
        catch (Exception e) {System.out.println("Could not update buffer "); e.printStackTrace();}
    }

    public boolean isReady() {
        try {
            return input.ready();
        } catch (Exception e) {System.out.println("Could not get state of buffer "); e.printStackTrace();}
        return false;
    }

    public boolean contains(String msg) {
        return buffer.contains(msg);
    }

    public boolean equals(String msg) {
        return buffer.equals(msg);
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    public boolean isBlank() {
        return buffer.isBlank();
    }
}
