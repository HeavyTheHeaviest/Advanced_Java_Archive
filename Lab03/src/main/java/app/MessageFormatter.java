package app;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageFormatter {

    public static String formatCoauthorsVerification(String title, int coauthorsCount, ResourceBundle bundle) {
        String pattern = bundle.getString("verify.coauthors.dynamic");
        String coauthorsText = coauthorsCountToString(coauthorsCount);
        return MessageFormat.format(pattern, title, coauthorsText);
    }

    private static String coauthorsCountToString(int count) {
        if (count == 0) {
            return "0 współautorów";
        } else if (count == 1) {
            return "1 współautor";
        } else {
            return count + " współautorów";
        }
    }
}
