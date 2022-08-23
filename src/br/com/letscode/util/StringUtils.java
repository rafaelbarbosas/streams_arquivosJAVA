package br.com.letscode.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class StringUtils {
    public static String multiply(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String blankSpaces(int quantity) {
        return multiply(" ", quantity);
    }

    public static String addBlankSpacesToAllLines(String s, int quantity) {
        String spaces = blankSpaces(quantity);
        s = spaces.concat(s).replaceAll("\n", ConsoleUtils.NEW_LINE + spaces);
        return s;
    }

    public static String centralize(String s, int lineWidth) {
        return blankSpaces((lineWidth - s.length()) / 2).concat(s);
    }

    public static String centralizeBlock(String s, int lineWidth) {
        String[] lines = s.split("\n");
        int maxLineLength = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > maxLineLength) {
                maxLineLength = lines[i].length();
            }
        }
        int spacesQuantity = (lineWidth - maxLineLength) / 2;
        s = addBlankSpacesToAllLines(s, spacesQuantity);

        return s;
    }

    public static String[] addArgToList(String[] args, String newArg) {
        String[] newArgs = new String[args.length + 1];

        for (int i = 0; i < args.length; i++) {
            newArgs[i] = args[i];
        }

        newArgs[args.length] = newArg;

        return newArgs;
    }

    public static String[] removeArgFromList(String[] args, int index) {
        String[] newArgs = new String[args.length - 1];

        int pos = 0;
        for (int i = 0; i < args.length; i++) {
            if (i != index) {
                newArgs[pos++] = args[i];
            }
        }

        return newArgs;
    }

    public static String getCsvRepresentation(List<String> list, boolean includeQuotes) {
        final String csvRepresentation = list.stream()
                .collect(Collectors.joining(","));
        return includeQuotes ? "\"" + csvRepresentation + "\"" : csvRepresentation;
    }

    public static String getCsvRepresentation(List<String> list) {
        return getCsvRepresentation(list, false);
    }

    public static String getCsvRepresentation(Set<String> set, boolean includeQuotes) {
        return getCsvRepresentation(set.stream()
                .sorted()
                .collect(Collectors.toList()), includeQuotes);
    }

    public static String getCsvRepresentation(Set<String> set) {
        return getCsvRepresentation(set, false);
    }

    public static String surroundIfContains(String text, String surroundString, String containedString) {
        return text.contains(containedString) ? surroundString + text + surroundString : text;
    }
}
