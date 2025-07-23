package xmlprocessor;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.net.*;

public class XsltTransformer {

    public static void transformAndPrint(String xmlUrl, String xsltPath) {

        File xsltFile = new File(xsltPath);
        System.out.println("Ładuję XSLT z pliku: " + xsltFile.getAbsolutePath());

        try (InputStream xmlStream = new URL(xmlUrl).openStream()) {
            TransformerFactory tf = TransformerFactory.newInstance();
            StreamSource xsltSource = new StreamSource(xsltFile);
            Transformer transformer = tf.newTransformer(xsltSource);

            StreamSource xmlSource = new StreamSource(xmlStream);

            StringWriter out = new StringWriter();
            transformer.transform(xmlSource, new StreamResult(out));
            String result = out.toString();

            if (result.isBlank()) {
                System.err.println("Transformacja zwróciła pusty wynik!");
            } else {
                System.out.println(result);
            }

        } catch (Exception e) {
            System.err.println("Błąd podczas transformacji XSLT:");
            e.printStackTrace();
        }
    }
}
