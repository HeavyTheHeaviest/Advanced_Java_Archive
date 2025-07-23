package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.*;

public class ApiClient {
    private static final String API_URL = "https://wolnelektury.pl/api/books";

    public List<JSONObject> getBooks() {
        List<JSONObject> books = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            JSONArray arr = new JSONArray(response.toString());
            for (int i = 0; i < arr.length(); i++) {
                books.add(arr.getJSONObject(i));
            }
        } catch (Exception e) {
            System.err.println("Błąd pobierania danych z API. Używam danych przykładowych.");
            books = getDummyBooks();
        }
        return books;
    }

    private List<JSONObject> getDummyBooks() {
        List<JSONObject> dummy = new ArrayList<>();
        try {
            JSONObject book1 = new JSONObject("{\"title\": \"Pan Tadeusz\", \"authors\": [\"Adam Mickiewicz\"], \"subject\": \"epika\"}");
            dummy.add(book1);
            JSONObject book2 = new JSONObject("{\"title\": \"Lalka\", \"authors\": [\"Bolesław Prus\"], \"subject\": \"realizm\"}");
            dummy.add(book2);
            JSONObject book3 = new JSONObject("{\"title\": \"Quo Vadis\", \"authors\": [\"Henryk Sienkiewicz\"], \"subject\": \"historyczna\"}");
            dummy.add(book3);
            JSONObject book4 = new JSONObject("{\"title\": \"Faraon\", \"authors\": [\"Bolesław Prus\"], \"subject\": \"historyczna\"}");
            dummy.add(book4);
            JSONObject book5 = new JSONObject("{\"title\": \"Krzyżacy\", \"authors\": [\"Henryk Sienkiewicz\"], \"subject\": \"historyczna\"}");
            dummy.add(book5);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return dummy;
    }
}

