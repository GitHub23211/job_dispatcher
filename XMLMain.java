import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

public class XMLMain extends Parser {

    public void parse() {
        try {
            String filename = "./ds-system.xml";
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser parser = spf.newSAXParser();
            XMLReader xmlReader =  parser.getXMLReader();
            xmlReader.setContentHandler(new XMLParser(servers));
            xmlReader.parse(getFileURL(filename));
        } catch (Exception e) {System.err.println("Error parsing XML: " + e);}

    }

    private static String getFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
}
