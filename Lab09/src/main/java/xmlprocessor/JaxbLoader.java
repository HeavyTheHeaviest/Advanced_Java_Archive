package xmlprocessor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.net.URL;

public class JaxbLoader {
    public static void loadAndPrint(String xmlUrl) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Channel.class);
            Unmarshaller um = ctx.createUnmarshaller();
            Channel channel = (Channel) um.unmarshal(new URL(xmlUrl));

            System.out.println("Kanał: " + channel.getTitle());
            System.out.println("Link: " + channel.getLink());
            System.out.println(channel.getDescription());
            System.out.println("---- Pozycje ----");

            channel.getItems().forEach(item ->
                    System.out.printf(
                            "%s | kurs: %s | odwrotny: %s%n",
                            item.getTargetCurrency(),
                            item.getExchangeRate(),
                            item.getInverseRate(),
                            item.getDate()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}