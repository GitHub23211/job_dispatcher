public class Buffer {
    private String buffer;

    Buffer() {
        this.buffer = "";
    }

    public String get() {
        return buffer;
    }

    public void update(String buffer) {
        this.buffer = buffer;
    }
}
