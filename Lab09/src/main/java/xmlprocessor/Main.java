package xmlprocessor;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar xmlprocessor.jar <mode> <xml-url> [xslt-path]");
            System.out.println("Modes: jaxb | dom | sax | xslt");
            return;
        }
        String mode = args[0];
        String xmlUrl = args[1];
        switch (mode.toLowerCase()) {
            case "jaxb":
                JaxbLoader.loadAndPrint(xmlUrl);
                break;
            case "dom":
                DomParser.parseAndPrint(xmlUrl);
                break;
            case "sax":
                SaxParser.parseAndPrint(xmlUrl);
                break;
            case "xslt":
                if (args.length < 3) {
                    System.err.println("Please provide path to XSLT file.");
                    break;
                }
                XsltTransformer.transformAndPrint(xmlUrl, args[2]);
                break;
            default:
                System.err.println("Unknown mode: " + mode);
        }
    }
}
