import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList; 

public class Parser {

    private static ArrayList<String> parse(String reg, String str, int index) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        ArrayList<String> server = new ArrayList<String>();
        if(m.find()) {

	server.add(m.group(1));
	server.add(m.group(3));
	server.add(m.group(9));
	

            return server;
        }
        server.add("error");
        return server;
    }

    public static ArrayList<String> getServerInfo(String msg) throws Exception {
        try {
            String reg = "([^ ]+)([ ])([^ ]+)([ ])([^ ]+)([ ])([^ ]+)([ ])([^ ]+)";
            return parse(reg, msg, 1);
        } catch (Exception e) {throw new Exception("Could not find largest server ID. Error: " + e);}
    }

    public static ArrayList<String> getMaxNumServers(String msg) throws Exception {
    try {
        String reg = "([^ ]+)([ ])([0-9]+)";
        return parse(reg, msg, 3);
    } catch (Exception e) {throw new Exception("Could not find largest number of servers. Error: " + e);}
}

    public static ArrayList<String> getJobId(String msg) throws Exception {
        try {
            String reg = "([a-zA-Z ])+[0-9]+([ ]+[0-9]+)";
            return parse(reg, msg, 2);
        }
        catch (Exception e) {throw new Exception("Could not get Job Id. Error: " + e);}
    }
}
