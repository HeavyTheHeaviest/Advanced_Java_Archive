package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataCache {
    private Map<String, WeakReference<MeasurementData>> cache = new HashMap<>();

    public MeasurementData getMeasurementData(File file) {
        String key = file.getAbsolutePath();
        WeakReference<MeasurementData> ref = cache.get(key);
        MeasurementData data = (ref != null) ? ref.get() : null;

        if (data != null) {
            System.out.println("Dane pobrane z pamięci dla: " + key);
            return data;
        } else {
            data = loadDataFromFile(file);
            cache.put(key, new WeakReference<>(data));
            System.out.println("Dane załadowane ponownie dla: " + key);
            return data;
        }
    }

    private MeasurementData loadDataFromFile(File file) {
        MeasurementData data = new MeasurementData();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                line = line.trim();

                String[] parts = line.split(",\\s*");

                if (parts.length < 4) {
                    System.out.println("Niepoprawny wiersz: " + Arrays.toString(parts));
                    continue;
                }

                data.addRow(parts);
            }

            data.calculateAverages();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
