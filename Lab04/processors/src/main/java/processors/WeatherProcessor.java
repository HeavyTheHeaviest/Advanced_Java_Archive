package processors;

import reflectionapi.Processor;
import java.util.Random;

public class WeatherProcessor implements Processor {
    private final Random random = new Random();

    @Override
    public String getInfo() {
        return "pogoda: miasto, data";
    }

    @Override
    public String process(String task) {
        String[] parts = task.split(",");
        if (parts.length < 2) {
            return "Błąd: nieprawidłowy format zadania. Oczekiwany format: 'miasto, data'";
        }
        String city = parts[0].trim();
        int temperature = random.nextInt(16) + 10;
        int pressure = random.nextInt(101) + 1000;
        return String.format("%s: %d stopni C, %d hPa", city, temperature, pressure);
    }
}
