package br.com.letscode.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class StringUtils {
    public static String getCsvRepresentation(List<String> list, boolean includeQuotes) {
        final String csvRepresentation = list.stream()
                .collect(Collectors.joining(","));
        return includeQuotes ? "\"" + csvRepresentation + "\"" : csvRepresentation;
    }

    public static String getCsvRepresentation(List<String> list) {
        return getCsvRepresentation(list, false);
    }

    public static String getCsvRepresentation(Set<String> set, boolean includeQuotes) {
        return getCsvRepresentation(set.stream().sorted().toList(), includeQuotes);
    }

    public static String getCsvRepresentation(Set<String> set) {
        return getCsvRepresentation(set.stream().sorted().toList(), false);
    }

    public static String surroundIfContains(String text, String surroundString, String containedString) {
        return text.contains(containedString) ? surroundString + text + surroundString : text;
    }
}
