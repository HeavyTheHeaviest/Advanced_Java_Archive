package app;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;

public class MeasurementApp extends JFrame {
    private JTree fileTree;
    private JPanel previewPanel;
    private DataCache dataCache = new DataCache();

    public MeasurementApp() {
        super("Przeglądarka Danych Pomiarowych");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        File rootDir = new File("pomiaryCSV");
        DefaultMutableTreeNode rootNode = createNodes(rootDir);
        fileTree = new JTree(rootNode);
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (node == null) return;
                Object userObject = node.getUserObject();
                if (userObject instanceof File) {
                    File selectedFile = (File) userObject;
                    if (selectedFile.isFile() && selectedFile.getName().endsWith(".csv")) {
                        MeasurementData data = dataCache.getMeasurementData(selectedFile);
                        showPreview(data);
                    }
                }
            }
        });

        JScrollPane treeScroll = new JScrollPane(fileTree);
        treeScroll.setPreferredSize(new Dimension(300, 600));
        add(treeScroll, BorderLayout.WEST);

        previewPanel = new JPanel(new BorderLayout());
        add(previewPanel, BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode createNodes(File fileRoot) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileRoot);
        if (fileRoot.isDirectory()) {
            File[] files = fileRoot.listFiles();
            if (files != null) {
                for (File file : files) {
                    node.add(createNodes(file));
                }
            }
        }
        return node;
    }

    private void showPreview(MeasurementData data) {
        previewPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int maxRows = Math.min(5, data.getRows().size());
        for (int i = 0; i < maxRows; i++) {
            String[] row = data.getRows().get(i);
            JLabel label = new JLabel(String.join(" | ", row));
            panel.add(label);
        }

        JLabel avgLabel = new JLabel(String.format(
                "Średnie - Ciśnienie: %.2f hPa, Temperatura: %.2f °C, Wilgotność: %.2f%%",
                data.getAvgPressure(), data.getAvgTemperature(), data.getAvgHumidity()
        ));
        panel.add(avgLabel);

        previewPanel.add(panel, BorderLayout.CENTER);
        previewPanel.revalidate();
        previewPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MeasurementApp app = new MeasurementApp();
            app.setVisible(true);
        });
    }
}
