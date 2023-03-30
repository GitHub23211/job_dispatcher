import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList; 

public class Parser {

    public static ArrayList<String> getServerInfo(String msg) throws Exception {
        try {
            String reg = "([^ ]+)([ ])([^ ]+)([ ])([^ ]+)([ ])([^ ]+)([ ])([^ ]+)";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(msg);
            ArrayList<String> server = new ArrayList<String>();
            if(m.find()) {
                server.add(m.group(1));
                server.add(m.group(3));
                server.add(m.group(9));
                return server;
            }
            server.add("error");
            return server;
        } catch (Exception e) {throw new Exception("Could not find largest server ID. Error: " + e);}
    }

    public static String getJobId(String msg) throws Exception {
        try {
            String reg = "([a-zA-Z ])+[0-9]+([ ]+[0-9]+)";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(msg);
            if(m.find()) {
                return m.group(2);
            }
            return "";
        }
        catch (Exception e) {throw new Exception("Could not get Job Id. Error: " + e);}
    }
}
