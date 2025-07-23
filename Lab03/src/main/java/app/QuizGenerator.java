package app;

import java.util.*;
import org.json.*;
import java.text.MessageFormat;

public class QuizGenerator {

    private static final int MAX_ATTEMPTS = 20;

    public enum QuestionType {
        AUTHOR, COAUTHORS, MULTIPLE, SUBJECT, AUTHOR_COUNT
    }

    public static class QuizQuestion {
        public QuestionType type;
        public Map<String, Object> params;
        public String questionText;
        public String verificationText;
        public String correctAnswer;

        public QuizQuestion(QuestionType type, Map<String, Object> params, String correctAnswer) {
            this.type = type;
            this.params = params;
            this.correctAnswer = correctAnswer;
        }

        public void updateLocalization(ResourceBundle bundle) {
            switch (type) {
                case AUTHOR: {
                    String title = (String) params.get("title");
                    @SuppressWarnings("unchecked")
                    List<String> authors = (List<String>) params.get("authors");
                    String questionTemplate = bundle.getString("question.author");
                    questionText = MessageFormat.format(questionTemplate, title);

                    String verifyTemplate = bundle.getString("verify.author");
                    verificationText = MessageFormat.format(verifyTemplate, title, String.join(", ", authors));
                    break;
                }
                case COAUTHORS: {
                    String title = (String) params.get("title");
                    Integer count = (Integer) params.get("count");
                    String questionTemplate = bundle.getString("question.coauthors");
                    questionText = MessageFormat.format(questionTemplate, title);
                    verificationText = MessageFormatter.formatCoauthorsVerification(title, count, bundle);
                    break;
                }
                case MULTIPLE: {
                    @SuppressWarnings("unchecked")
                    List<String> titles = (List<String>) params.get("titles");
                    String subject = (String) params.get("subject");
                    String questionTemplate = bundle.getString("question.multiple");
                    questionText = MessageFormat.format(questionTemplate, titles.get(0), titles.get(1),
                            titles.get(2), titles.get(3), subject);

                    String correctTitle = titles.get((Integer) params.get("correctIndex"));
                    String verifyTemplate = bundle.getString("verify.multiple");
                    verificationText = MessageFormat.format(verifyTemplate, correctTitle, subject);
                    break;
                }
                case SUBJECT: {
                    String title = (String) params.get("title");
                    String subject = (String) params.get("subject");
                    String questionTemplate = bundle.getString("question.subject");
                    questionText = MessageFormat.format(questionTemplate, title);

                    String verifyTemplate = bundle.getString("verify.subject");
                    verificationText = MessageFormat.format(verifyTemplate, title, subject);
                    break;
                }
                case AUTHOR_COUNT: {
                    String author = (String) params.get("author");
                    Integer count = (Integer) params.get("count");
                    String questionTemplate = bundle.getString("question.authorCount");
                    questionText = MessageFormat.format(questionTemplate, author);

                    // Możesz użyć osobnej metody formatującej liczbę książek, jeśli chcesz
                    String verifyTemplate = bundle.getString("verify.authorCount");
                    verificationText = MessageFormat.format(verifyTemplate, author, count);
                    break;
                }
            }
        }
    }

    private final Random rand = new Random();

    public QuizQuestion generateRandomQuestion(List<JSONObject> books, ResourceBundle bundle) {
        if (books == null || books.isEmpty()) {
            return null;
        }

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            QuestionType[] types = QuestionType.values();
            QuestionType chosenType = types[rand.nextInt(types.length)];

            QuizQuestion question = null;
            switch (chosenType) {
                case AUTHOR:
                    question = generateAuthorQuestion(books);
                    break;
                case COAUTHORS:
                    question = generateCoauthorsQuestion(books);
                    break;
                case MULTIPLE:
                    question = generateMultipleChoiceQuestion(books);
                    break;
                case SUBJECT:
                    question = generateSubjectQuestion(books);
                    break;
                case AUTHOR_COUNT:
                    question = generateAuthorCountQuestion(books);
                    break;
            }
            if (question != null) {
                question.updateLocalization(bundle);
                return question;
            }
        }
        return null;
    }


    private QuizQuestion generateAuthorQuestion(List<JSONObject> books) {
        JSONObject book = getRandomBook(books);
        String title = book.optString("title", "nieznany tytuł");
        JSONArray authorsArray = book.optJSONArray("authors");
        List<String> authors = new ArrayList<>();
        if (authorsArray != null) {
            for (int i = 0; i < authorsArray.length(); i++) {
                authors.add(authorsArray.optString(i));
            }
        }
        if (authors.isEmpty() || authors.contains("nieznany autor")) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("authors", authors);
        String correctAnswer = String.join(";", authors);
        return new QuizQuestion(QuestionType.AUTHOR, params, correctAnswer);
    }


    private QuizQuestion generateCoauthorsQuestion(List<JSONObject> books) {
        JSONObject book = getRandomBook(books);
        String title = book.optString("title", "nieznany tytuł");
        JSONArray authorsArray = book.optJSONArray("authors");
        if (title.equals("nieznany tytuł") || authorsArray == null) {
            return null;
        }
        int count = authorsArray.length();

        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("count", count);
        String correctAnswer = Integer.toString(count);
        return new QuizQuestion(QuestionType.COAUTHORS, params, correctAnswer);
    }

    private QuizQuestion generateMultipleChoiceQuestion(List<JSONObject> books) {
        if (books.size() < 4) {
            return null;
        }
        List<JSONObject> choices = new ArrayList<>();
        Set<Integer> indices = new HashSet<>();
        while (indices.size() < 4) {
            indices.add(rand.nextInt(books.size()));
        }
        for (Integer idx : indices) {
            choices.add(books.get(idx));
        }
        String subject = "klasyka";
        JSONObject sampleBook = choices.get(0);
        if (sampleBook.has("subject")) {
            subject = sampleBook.optString("subject");
        }
        List<String> titles = new ArrayList<>();
        for (JSONObject choice : choices) {
            titles.add(choice.optString("title", "brak"));
        }
        if (titles.stream().allMatch(t -> t.equals("brak"))) {
            return null;
        }
        int correctIndex = rand.nextInt(4);

        Map<String, Object> params = new HashMap<>();
        params.put("titles", titles);
        params.put("subject", subject);
        params.put("correctIndex", correctIndex);

        String correctAnswer = new String[]{"a", "b", "c", "d"}[correctIndex];
        return new QuizQuestion(QuestionType.MULTIPLE, params, correctAnswer);
    }

    private QuizQuestion generateSubjectQuestion(List<JSONObject> books) {
        JSONObject book = getRandomBook(books);
        String title = book.optString("title", "nieznany tytuł");
        if (title.equals("nieznany tytuł")) {
            return null;
        }
        String subject = book.optString("subject", getRandomSubject());
        if (subject.isEmpty() || subject.equals("null")) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("subject", subject);
        return new QuizQuestion(QuestionType.SUBJECT, params, subject);
    }

    private QuizQuestion generateAuthorCountQuestion(List<JSONObject> books) {
        List<String> allAuthors = new ArrayList<>();
        for (JSONObject book : books) {
            JSONArray arr = book.optJSONArray("authors");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    String auth = arr.optString(i);
                    if (!allAuthors.contains(auth)) {
                        allAuthors.add(auth);
                    }
                }
            }
        }
        if (allAuthors.isEmpty() || allAuthors.contains("nieznany autor")) {
            return null;
        }
        String chosenAuthor = allAuthors.get(rand.nextInt(allAuthors.size()));
        int count = 0;
        for (JSONObject book : books) {
            JSONArray arr = book.optJSONArray("authors");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    if (chosenAuthor.equalsIgnoreCase(arr.optString(i))) {
                        count++;
                        break;
                    }
                }
            }
        }
        if (count == 0) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("author", chosenAuthor);
        params.put("count", count);
        String correctAnswer = Integer.toString(count);
        return new QuizQuestion(QuestionType.AUTHOR_COUNT, params, correctAnswer);
    }

    private JSONObject getRandomBook(List<JSONObject> books) {
        return books.get(rand.nextInt(books.size()));
    }

    private String getRandomSubject() {
        String[] subjects = {"klasyka", "romantyzm", "historia", "filozofia", "przygoda"};
        return subjects[rand.nextInt(subjects.length)];
    }
}
