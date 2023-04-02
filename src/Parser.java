public abstract class Parser {

    protected String name;
    protected int max;

    Parser() {
        this.name = "";
        this.max = 0;
    }

    public abstract void parse();

    public String getServerName() {
        return name;
    }

    public int getMaxServerCount() {
        return max;
    }

}