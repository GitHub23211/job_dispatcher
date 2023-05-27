import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Matcher createParser(String msg, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        return m;
    }

    public static Server parseServerInfo(String msg) {
        String reg = "(?<name>[^ ]+)[ ](?<id>[^ ]+)[ ](?<state>[^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Matcher m = createParser(msg, reg);
        if(m.find()) {
            String name = m.group("name");
            String id = m.group("id");
            String state = m.group("state");
            int cores = Integer.parseInt(m.group("cores"));
            return new Server(name, id, state, cores);
        }
        return new Server();
    }

    public static Job parseJobInfo(String msg) {
        String reg = "(?<name>[^ ]+)[ ]([^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Matcher m = createParser(msg, reg);
        if(m.find()) {
            String name = m.group("name");
            String id = m.group("id");
            int cores = Integer.parseInt(m.group("cores"));
            int mem = Integer.parseInt(m.group("memory"));
            int disk = Integer.parseInt(m.group("disk"));
            return new Job(name, id, cores, mem, disk);
        }
        return new Job();
    }

    public static Job parseJobinQ(String msg) {
        String reg = "(?<name>[^ ]+)[ ]([^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)";
        Matcher m = createParser(msg, reg);
        if(m.find()) {
            String name = m.group("name");
            String id = m.group("id");
            int cores = Integer.parseInt(m.group("cores"));
            return new Job(name, id, cores);
        }
        return new Job();
    }

    public static int parseLSTJ(String msg) {
        String reg = "(([^ ]+)[ ])+(?<cores>[0-9])[ ](?<memory>[^ ]+)[ ](?<disk>[^ ]+)";
        Matcher m = createParser(msg, reg);
        if(m.find()) {
            return Integer.parseInt(m.group("cores"));
        }
        return 0;
    }

    public static String[] parseJCPL(String msg) {
        String reg = "(([^ ]+)[ ])+(?<name>[^ ]+)[ ](?<id>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            String name = m.group("name");
            String id = m.group("id");
            return new String[] {name, id};
        }
        return new String[] {"error"};
    }

}