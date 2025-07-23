package app;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import org.json.*;

public class QuizFrame extends JFrame {
    private Locale currentLocale;
    private ResourceBundle bundle;
    private JLabel questionLabel;
    private JTextField answerField;
    private JLabel resultLabel;
    private JButton submitButton;
    private JButton skipButton;
    private JButton languageButton;

    private List<JSONObject> books;
    private QuizGenerator quizGenerator;
    private QuizGenerator.QuizQuestion currentQuestion;

    public QuizFrame() {
        currentLocale = new Locale("pl", "PL");
        loadResourceBundle();
        setTitle("Quiz Bibliograficzny");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 400);
        setLayout(new BorderLayout(10,10));
        ApiClient apiClient = new ApiClient();
        books = apiClient.getBooks();

        quizGenerator = new QuizGenerator();

        initComponents();
        loadRandomQuestion();
        setLocationRelativeTo(null);
    }

    private void loadResourceBundle() {
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridLayout(3,1,5,5));
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        answerField = new JTextField();
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(questionLabel);
        centerPanel.add(answerField);
        centerPanel.add(resultLabel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        submitButton = new JButton(bundle.getString("button.submit"));
        submitButton.addActionListener(e -> checkAnswer());
        skipButton = new JButton(bundle.getString("button.skip"));
        skipButton.addActionListener(e -> loadRandomQuestion());
        bottomPanel.add(submitButton);
        bottomPanel.add(skipButton);
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        languageButton = new JButton(bundle.getString("button.changeLanguage"));
        languageButton.addActionListener(e -> toggleLanguage());
        topPanel.add(languageButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private void loadRandomQuestion() {
        if (books.isEmpty()) {
            questionLabel.setText("Brak danych o książkach.");
            return;
        }
        currentQuestion = quizGenerator.generateRandomQuestion(books, bundle);
        if (currentQuestion == null) {
            questionLabel.setText("Niestety, nie można teraz wygenerować pytania.");
            answerField.setText("");
            resultLabel.setText("");
            return;
        }
        questionLabel.setText(currentQuestion.questionText);
        answerField.setText("");
        resultLabel.setText("");
    }

    private void checkAnswer() {
        String userAnswer = answerField.getText().trim();
        boolean correct = false;

        switch (currentQuestion.type) {
            case AUTHOR: {
                String[] correctAuthors = currentQuestion.correctAnswer.split(";");
                for (String auth : correctAuthors) {
                    if (userAnswer.equalsIgnoreCase(auth.trim())) {
                        correct = true;
                        break;
                    }
                }
                break;
            }
            case COAUTHORS:
                correct = userAnswer.equals(currentQuestion.correctAnswer);
                break;
            case MULTIPLE:
                correct = userAnswer.equalsIgnoreCase(currentQuestion.correctAnswer);
                break;
            case SUBJECT:
                correct = userAnswer.equalsIgnoreCase(currentQuestion.correctAnswer);
                break;
            case AUTHOR_COUNT:
                correct = userAnswer.equals(currentQuestion.correctAnswer);
                break;
        }

        if (correct) {
            resultLabel.setForeground(Color.GREEN.darker());
            resultLabel.setText(currentQuestion.verificationText);
        } else {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText(bundle.getString("verify.incorrect"));
        }
    }

    private void toggleLanguage() {
        if (currentLocale.getLanguage().equals("pl")) {
            currentLocale = new Locale("en", "US");
        } else {
            currentLocale = new Locale("pl", "PL");
        }
        loadResourceBundle();
        submitButton.setText(bundle.getString("button.submit"));
        skipButton.setText(bundle.getString("button.skip"));
        languageButton.setText(bundle.getString("button.changeLanguage"));
        if (currentQuestion != null) {
            currentQuestion.updateLocalization(bundle);
            questionLabel.setText(currentQuestion.questionText);
            answerField.setText("");
            resultLabel.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizFrame().setVisible(true));
    }
}
