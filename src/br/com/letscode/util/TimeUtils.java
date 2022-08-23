package br.com.letscode.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String nowString() {
        LocalDateTime now = LocalDateTime.now();
        return DEFAULT_FORMATTER.format(now);
    }

    public static String nowString(String dateTimeFormatterPattern) {
        LocalDateTime now = LocalDateTime.now();
        return DateTimeFormatter.ofPattern(dateTimeFormatterPattern).format(now);
    }
}
