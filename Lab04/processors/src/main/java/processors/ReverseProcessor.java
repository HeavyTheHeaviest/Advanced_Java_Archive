package processors;

import reflectionapi.Processor;

public class ReverseProcessor implements Processor {
    @Override
    public String getInfo() {
        return "odwroc: zdanie";
    }

    @Override
    public String process(String task) {
        return new StringBuilder(task).reverse().toString();
    }
}
