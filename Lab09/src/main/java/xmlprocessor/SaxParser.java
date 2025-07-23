package xmlprocessor;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;

public class SaxParser {
    public static void parseAndPrint(String xmlUrl) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            sp.parse(new URL(xmlUrl).openStream(), new ItemHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ItemHandler extends DefaultHandler {
        private String currentElement;
        private String targetCurrency;
        private String exchangeRate;
        private String inverseRate;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            currentElement = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            String text = new String(ch, start, length).trim();
            if (text.isEmpty() || currentElement == null) return;
            switch (currentElement) {
                case "targetCurrency": targetCurrency = text; break;
                case "exchangeRate": exchangeRate = text; break;
                case "inverseRate": inverseRate = text; break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("item".equals(qName)) {
                System.out.printf("%s | kurs: %s | odwrotny: %s%n",
                        targetCurrency, exchangeRate, inverseRate);
                targetCurrency = exchangeRate = inverseRate = null;
            }
            currentElement = null;
        }
    }
}