package xmlprocessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

public class DomParser {
    public static void parseAndPrint(String xmlUrl) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(xmlUrl).openStream());

            Element channelEl = (Element) doc.getElementsByTagNameNS("*", "channel").item(0);
            if (channelEl != null) {
                String title = channelEl.getElementsByTagNameNS("*", "title").item(0).getTextContent();
                System.out.println("Kanał: " + title);
            }

            NodeList itemNodes = doc.getElementsByTagNameNS("*", "item");
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element itemEl = (Element) itemNodes.item(i);
                String target = getTextContent(itemEl, "targetCurrency");
                String rate = getTextContent(itemEl, "exchangeRate");
                String inv = getTextContent(itemEl, "inverseRate");
                System.out.printf("%s | kurs: %s | odwrotny: %s%n", target, rate, inv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTextContent(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagNameNS("*", tag);
        return (nl.getLength() > 0 && nl.item(0) != null)
                ? nl.item(0).getTextContent() : "";
    }
}