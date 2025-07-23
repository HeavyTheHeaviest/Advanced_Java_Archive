package analysisapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import ex.api.AnalysisService;
import ex.api.DataSet;
import ex.api.AnalysisException;
import java.awt.*;
import java.awt.event.*;
import java.util.ServiceLoader;

public class AnalysisApp extends JFrame {
    private JTable table;
    private JComboBox<AnalysisService> algorithmComboBox;
    private JTextArea resultArea;
    private JButton calculateButton;
    private ServiceLoader<AnalysisService> serviceLoader;

    public AnalysisApp() {
        initComponents();
        loadServices();
    }

    private void initComponents() {
        setTitle("Analiza Tabel Niezgodności");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Utworzenie tabeli – przykładowo 5 wierszy i 5 kolumn
        DefaultTableModel tableModel = new DefaultTableModel(5, 5);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll, BorderLayout.CENTER);

        // Panel sterowania z wyborem algorytmu i przyciskiem "Oblicz"
        JPanel controlPanel = new JPanel(new FlowLayout());
        algorithmComboBox = new JComboBox<>();
        controlPanel.add(new JLabel("Wybierz algorytm:"));
        controlPanel.add(algorithmComboBox);

        calculateButton = new JButton("Oblicz");
        controlPanel.add(calculateButton);
        add(controlPanel, BorderLayout.NORTH);

        // Obszar wyświetlania wyników
        resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // Reakcja na przycisk "Oblicz"
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateMetrics();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void loadServices() {
        // Ładowanie implementacji interfejsu AnalysisService za pomocą SPI
        serviceLoader = ServiceLoader.load(AnalysisService.class);
        for (AnalysisService service : serviceLoader) {
            algorithmComboBox.addItem(service);
        }
        if (algorithmComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono implementacji AnalysisService.",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateMetrics() {
        try {
            // Pobranie danych z tabeli – tworzymy macierz String[][] z wartościami wpisanymi w tabeli
            int rowCount = table.getRowCount();
            int colCount = table.getColumnCount();
            String[][] data = new String[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    Object value = table.getValueAt(i, j);
                    data[i][j] = value != null ? value.toString() : "0";
                }
            }
            // Utwórz obiekt DataSet
            DataSet ds = new DataSet();
            ds.setData(data);

            // Pobranie wybranej implementacji
            AnalysisService service = algorithmComboBox.getItemAt(algorithmComboBox.getSelectedIndex());
            // Jeśli implementacja posiada dodatkową metodę analyze(), możemy ją wykorzystać
            String result;
            // Przykładowe rozwiązanie: jeżeli usługa to nasza implementacja z my.metrics, rzutujemy
            if (service.getClass().getName().contains("SimpleMetricsService")) {
                result = ((metrics.SimpleMetricsService) service).analyze(ds);
            } else {
                // Alternatywnie: wykonaj submit i retrieve
                service.submit(ds);
                DataSet resultDS = service.retrieve(true);
                result = resultDS != null ? "Wyniki analizy zapisane w DataSet" : "Brak wyników";
            }
            resultArea.setText(result);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Błąd formatu danych: " + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (AnalysisException ex) {
            JOptionPane.showMessageDialog(this, "Błąd analizy: " + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnalysisApp().setVisible(true);
        });
    }
}
