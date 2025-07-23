package application;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MainController {

    @FXML private ComboBox<String> categoryBox;
    @FXML private TextArea outputArea;

    @FXML
    private void initialize() {
        // inicjalizacja listy kategorii
        categoryBox.getItems().setAll("life", "motivation", "humor");
    }

    @FXML
    private void generateQuote() {
        try {
            String category = categoryBox.getValue();
            if (category == null) category = "life";
            var uri = getClass().getResource("/patterns/" + category + ".txt").toURI();
            List<String> lines = Files.readAllLines(Path.of(uri));
            if (lines.isEmpty()) {
                outputArea.setText("Brak cytatów w kategorii " + category);
            } else {
                String quote = lines.get(new Random().nextInt(lines.size()));
                outputArea.setText(quote);
            }
        } catch (Exception ex) {
            outputArea.setText("Błąd: " + ex.getMessage());
        }
    }
}
