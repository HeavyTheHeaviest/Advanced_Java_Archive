package reflectiontaskmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import reflectionapi.Processor;

/**
 * Główna klasa aplikacji – interfejs graficzny umożliwiający:
 * 1. Określenie folderu z plikami .class,
 * 2. Ładowanie klas implementujących interfejs Processor przy użyciu niestandardowego ładowacza,
 * 3. Wywoływanie metody process() dla wybranego procesora,
 * 4. Wyświetlanie wyników.
 */
public class ReflectionTaskManager extends JFrame {
    private JTextField directoryField;
    private JButton refreshButton;
    private DefaultListModel<LoadedProcessor> processorListModel;
    private JList<LoadedProcessor> processorList;
    private JTextField taskInputField;
    private JButton executeButton;
    private JButton unloadButton;
    private JTextArea outputArea;

    // Lista przechowująca załadowane procesory
    private List<LoadedProcessor> loadedProcessors = new ArrayList<>();

    public ReflectionTaskManager() {
        super("Menadżer zadań - Reflection API");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel górny – ustawienie folderu z klasami
        JPanel topPanel = new JPanel(new BorderLayout());
        directoryField = new JTextField(System.getProperty("user.dir"));
        refreshButton = new JButton("Odśwież klasy");
        topPanel.add(new JLabel("Ścieżka do folderu z klasami:"), BorderLayout.WEST);
        topPanel.add(directoryField, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Panel po lewej – lista załadowanych procesorów
        processorListModel = new DefaultListModel<>();
        processorList = new JList<>(processorListModel);
        JScrollPane listScrollPane = new JScrollPane(processorList);
        listScrollPane.setPreferredSize(new Dimension(250, 400));
        add(listScrollPane, BorderLayout.WEST);

        // Panel centralny – definicja zadania i przyciski
        JPanel centerPanel = new JPanel(new BorderLayout());
        taskInputField = new JTextField();
        executeButton = new JButton("Wykonaj zadanie");
        unloadButton = new JButton("Wyładuj klasę");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(executeButton);
        buttonPanel.add(unloadButton);
        centerPanel.add(new JLabel("Definicja zadania:"), BorderLayout.NORTH);
        centerPanel.add(taskInputField, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Panel dolny – obszar wyjściowy
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(800, 150));
        add(outputScrollPane, BorderLayout.SOUTH);

        // Obsługa zdarzeń
        refreshButton.addActionListener(e -> loadProcessorClasses());
        executeButton.addActionListener(e -> executeTask());
        unloadButton.addActionListener(e -> unloadSelectedProcessor());

        // Debug: wypisanie katalogu roboczego
        System.out.println("Katalog roboczy: " + new File(".").getAbsolutePath());
    }

    private void loadProcessorClasses() {
        String dirPath = directoryField.getText().trim();
        File folder = new File(dirPath);
        if (!folder.isAbsolute()) {
            folder = folder.getAbsoluteFile();
        }
        if (!folder.exists() || !folder.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Nieprawidłowy folder: " + folder.getAbsolutePath(), "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Debug: wypisanie folderu, który będziemy przeszukiwać
        System.out.println("Skanuję folder: " + folder.getAbsolutePath());

        processorListModel.clear();
        loadedProcessors.clear();
        outputArea.setText("");

        MyClassLoader loader;
        try {
            loader = new MyClassLoader(folder.getAbsolutePath(), this.getClass().getClassLoader());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd tworzenia ładowacza klas: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Path basePath = folder.toPath();
        try (Stream<Path> stream = Files.walk(basePath)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".class"))
                    .filter(p -> !p.getFileName().toString().equals("module-info.class"))
                    .forEach(p -> {
                        System.out.println("Znaleziono plik: " + p.toAbsolutePath());
                        Path relativePath = basePath.relativize(p);
                        String className = relativePath.toString()
                                .replace(File.separatorChar, '.')
                                .replaceAll("\\.class$", "");
                        System.out.println("Próba załadowania klasy: " + className);
                        try {
                            Class<?> clazz = loader.loadClass(className);
                            if (Processor.class.isAssignableFrom(clazz)) {
                                Constructor<?> cons = clazz.getDeclaredConstructor();
                                cons.setAccessible(true);
                                Object instance = cons.newInstance();
                                Processor processor = (Processor) instance;
                                LoadedProcessor lp = new LoadedProcessor(className, processor, loader);
                                loadedProcessors.add(lp);
                                processorListModel.addElement(lp);
                            }
                        } catch (Exception ex) {
                            System.err.println("Błąd ładowania klasy " + className + ": " + ex.getMessage());
                        }
                    });
            outputArea.append("Załadowano " + loadedProcessors.size() + " klas.\n");
        } catch (IOException e) {
            outputArea.append("Błąd podczas przeszukiwania folderu: " + e.getMessage() + "\n");
        }
    }

    private void executeTask() {
        LoadedProcessor selected = processorList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Wybierz klasę procesora", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String taskDefinition = taskInputField.getText().trim();
        if (taskDefinition.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Podaj definicję zadania", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return selected.getProcessorInstance().process(taskDefinition);
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    outputArea.append("Wynik: " + result + "\n");
                } catch (Exception ex) {
                    outputArea.append("Błąd podczas wykonywania zadania: " + ex.getMessage() + "\n");
                }
            }
        };
        worker.execute();
    }

    private void unloadSelectedProcessor() {
        int index = processorList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz klasę do wyładowania", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LoadedProcessor lp = processorListModel.getElementAt(index);
        processorListModel.remove(index);
        loadedProcessors.remove(lp);
        outputArea.append("Wyładowano klasę: " + lp.getClassName() + "\n");
        System.gc();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReflectionTaskManager manager = new ReflectionTaskManager();
            manager.setVisible(true);
        });
    }
}
