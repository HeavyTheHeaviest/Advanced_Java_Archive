package app;

import javax.swing.*;

public class SimpleDataRenderer implements DataRenderer {
    @Override
    public JComponent render(MeasurementData data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int maxRows = Math.min(5, data.getRows().size());
        for (int i = 0; i < maxRows; i++) {
            String[] row = data.getRows().get(i);
            JLabel label = new JLabel(String.join(" | ", row));
            panel.add(label);
        }

        JLabel avgLabel = new JLabel(
                String.format("Średnie - Ciśnienie: %.2f hPa, Temperatura: %.2f °C, Wilgotność: %.2f%%",
                        data.getAvgPressure(), data.getAvgTemperature(), data.getAvgHumidity())
        );
        panel.add(avgLabel);
        return panel;
    }
}
