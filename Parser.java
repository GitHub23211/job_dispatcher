import java.util.ArrayList; 
public abstract class Parser {

    public ArrayList<Server> servers;
    public String name;
    public int max;

    Parser() {
        this.servers = new ArrayList<Server>();
        this.name = "";
        this.max = 0;
    }

    public abstract void parse();
    
    public void getLargestServer() {
        int maxCores = 0;
        for(Server s : servers) {
            int sCores =s.getCores();
            if(sCores > maxCores) {
                maxCores = sCores;
                max = s.getId();
                name = s.getName();
            }
        }
    }
}