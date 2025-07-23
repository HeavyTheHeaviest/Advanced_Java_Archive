package app;

public class AverageDataProcessor implements DataProcessor {
    @Override
    public void process(MeasurementData data) {
        data.calculateAverages();
    }
}

