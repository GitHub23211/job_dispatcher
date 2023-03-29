import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static String parse(String reg, String str, int index) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        if(m.find()) {
            return m.group(index);
        }
        return "";
    }

    public static String findLargestServer(String msg) throws Exception {
        try {
            String reg = "([^ ]+)([ ])([0-9]+)";
            return parse(reg, msg, 1);
        } catch (Exception e) {throw new Exception("Could not find largest server ID. Error: " + e);}
    }

    public static String getMaxNumServers(String msg) throws Exception {
    try {
        String reg = "([^ ]+)([ ])([0-9]+)";
        return parse(reg, msg, 3);
    } catch (Exception e) {throw new Exception("Could not find largest number of servers. Error: " + e);}
}

    public static String getJobId(String msg) throws Exception {
        try {
            String reg = "([a-zA-Z ])+[0-9]+([ ]+[0-9]+)";
            return parse(reg, msg, 2);
        }
        catch (Exception e) {throw new Exception("Could not get Job Id. Error: " + e);}
    }
}