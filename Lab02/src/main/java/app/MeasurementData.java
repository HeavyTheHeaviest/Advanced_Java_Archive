package app;

import java.util.ArrayList;
import java.util.List;

public class MeasurementData {
    private List<String[]> rows = new ArrayList<>();
    private double avgPressure;
    private double avgTemperature;
    private double avgHumidity;

    public void addRow(String[] row) {
        rows.add(row);
    }

    public List<String[]> getRows() {
        return rows;
    }

    public void calculateAverages() {
        double sumPressure = 0, sumTemperature = 0, sumHumidity = 0;
        int count = 0;

        for (String[] row : rows) {
            if (row.length < 4) {
                continue;
            }
            try {
                double pressure = Double.parseDouble(row[1]);
                double temperature = Double.parseDouble(row[2]);
                double humidity = Double.parseDouble(row[3]);
                sumPressure += pressure;
                sumTemperature += temperature;
                sumHumidity += humidity;
                count++;
            } catch (NumberFormatException e) {
                System.out.println("Nie udało się sparsować wiersza: " + String.join(",", row));
            }
        }

        if (count > 0) {
            avgPressure = sumPressure / count;
            avgTemperature = sumTemperature / count;
            avgHumidity = sumHumidity / count;
        }
    }

    public double getAvgPressure() {
        return avgPressure;
    }

    public double getAvgTemperature() {
        return avgTemperature;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }
}
