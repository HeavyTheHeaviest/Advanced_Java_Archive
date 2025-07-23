package metrics;

import ex.api.AnalysisService;
import ex.api.AnalysisException;
import ex.api.DataSet;

public class SimpleMetricsService implements AnalysisService {
    // Opcjonalne przechowywanie opcji algorytmu
    private String[] options;

    @Override
    public void setOptions(String[] options) throws AnalysisException {
        this.options = options;
    }

    @Override
    public String getName() {
        return "Simple Metrics Service";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        // W tym przykładzie analiza jest synchroniczna,
        // dlatego cała logika analizy jest realizowana w metodzie analyze.
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        // Tutaj można zwrócić DataSet zawierający wyniki analizy.
        // W naszym przypadku metoda analyze() zwraca raport w postaci String.
        return null;
    }

    // Metoda łącząca submit() i retrieve() – wykonuje analizę i zwraca raport
    public String analyze(DataSet ds) throws AnalysisException {
        // Przykładowe przetwarzanie – dane są pobierane jako macierz typu double[][]
        double[][] matrix = extractMatrix(ds);

        double kappa = computeKappa(matrix);
        double accuracy = computeAccuracy(matrix);
        double f1Score = computeF1Score(matrix);
        double balancedAccuracy = computeBalancedAccuracy(matrix);

        StringBuilder report = new StringBuilder();
        report.append("Wyniki analizy:\n");
        report.append("Kappa: ").append(kappa).append("\n");
        report.append("Accuracy: ").append(accuracy).append("\n");
        report.append("F1-Score: ").append(f1Score).append("\n");
        report.append("Balanced Accuracy: ").append(balancedAccuracy).append("\n");

        return report.toString();
    }

    // Przykładowa implementacja obliczenia współczynnika kappa (wartość przykładowa)
    private double computeKappa(double[][] matrix) {
        return 0.75; // przykładowa wartość
    }

    private double computeAccuracy(double[][] matrix) {
        double sumCorrect = 0;
        double total = 0;
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            sumCorrect += matrix[i][i];
            for (int j = 0; j < matrix[i].length; j++) {
                total += matrix[i][j];
            }
        }
        return total != 0 ? sumCorrect / total : 0;
    }

    // Prosta, przykładowa metoda – zazwyczaj należałoby obliczać precyzję i recall osobno dla każdej klasy
    private double computeF1Score(double[][] matrix) {
        return 0.65;
    }

    // Prosta metoda uśredniająca wartość recall z każdej klasy
    private double computeBalancedAccuracy(double[][] matrix) {
        int n = matrix.length;
        double sumRecall = 0;
        for (int i = 0; i < n; i++) {
            double rowSum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                rowSum += matrix[i][j];
            }
            double recall = rowSum != 0 ? matrix[i][i] / rowSum : 0;
            sumRecall += recall;
        }
        return n > 0 ? sumRecall / n : 0;
    }

    // Metoda pomocnicza konwertująca dane z DataSet na macierz double[][]
    private double[][] extractMatrix(DataSet ds) throws AnalysisException {
        String[][] stringData = ds.getData();
        int rows = stringData.length;
        int cols = stringData[0].length;
        double[][] matrix = new double[rows][cols];
        try {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = Double.parseDouble(stringData[i][j].trim());
                }
            }
        } catch (NumberFormatException e) {
            throw new AnalysisException("Błąd konwersji danych na liczby: " + e.getMessage());
        }
        return matrix;
    }
}
