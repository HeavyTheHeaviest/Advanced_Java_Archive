package processors;

import reflectionapi.Processor;

public class CSVFilterProcessor implements Processor {
    @Override
    public String getInfo() {
        return "csvfilter: ścieżka, kolumny";
    }

    @Override
    public String process(String task) {
        // Format zadania: "file:\C:\in.csv, 2:3:4"
        String[] parts = task.split(",");
        if (parts.length < 2) {
            return "Błąd: nieprawidłowy format zadania. Oczekiwany format: 'file:\\in.csv, kolumny'";
        }

        String fileUrl = parts[0].trim();
        String filterParams = parts[1].trim();

        if (fileUrl.toLowerCase().endsWith(".csv")) {
            fileUrl = fileUrl.substring(0, fileUrl.length() - 4) + ".filtered.csv";
        } else {
            fileUrl += ".filtered";
        }

        return fileUrl + " (przefiltrowano kolumny: " + filterParams + ")";
    }
}
