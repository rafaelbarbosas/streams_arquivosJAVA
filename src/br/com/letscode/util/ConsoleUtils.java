/*
 * Huge thanks to Janne Tuukkanen's answer on this
 * <https://stackoverflow.com/questions/48773272/write-print-to-the-bottom-of-terminal>
 */

package br.com.letscode.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.letscode.model.system.ConsolePosition;

public class ConsoleUtils {
    static final int DEFAULT_SPEED = 1200;

    public static final String ESC = "\033";
    public static final String NEW_LINE = System.lineSeparator();

    public enum Attribute {
        RESET(0),
        BRIGHT(1),
        USCORE(4),
        BLINK(5),
        REVERSE(7),

        FCOL_BLACK(30),
        FCOL_RED(31),
        FCOL_GREEN(32),
        FCOL_YELLOW(33),
        FCOL_BLUE(34),
        FCOL_PURPLE(35),
        FCOL_CYAN(36),
        FCOL_GRAY(37),

        BCOL_BLACK(40),
        BCOL_RED(41),
        BCOL_GREEN(42),
        BCOL_YELLOW(43),
        BCOL_BLUE(44),
        BCOL_PURPLE(45),
        BCOL_CYAN(46),
        BCOL_GRAY(47);

        private int code;

        private Attribute(int code) {
            this.code = code;
        }

        public String getEscapeCode() {
            return String.format("\033[%dm", this.code);
        }
    }

    public static void clearScreen() {
        System.out.print(Attribute.RESET.getEscapeCode() + "\033[2J");
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
        }
    }

    public static void cursorHome() {
        System.out.print("\033[H");
    }

    public static void cursorTo(int row, int column) {
        System.out.print(String.format("\033[%d;%dH", row, column));
    }

    public static void cursorTo(ConsolePosition pos) {
        cursorTo(pos.getRow(), pos.getColumn());
    }

    public static void cursorSave() {
        System.out.print("\033[s");
    }

    public static void cursorRestore() {
        System.out.print("\033[u");
    }

    public static void cursorRequest() {
        System.out.print("\u001b[6n");
    }

    public static void scrollScreen() {
        System.out.print("\033[r");
    }

    public static void skipLines(int quantity) {
        for (int i = 0; i < quantity; i++) {
            System.out.print(" " + NEW_LINE);
        }
    }

    public static ConsolePosition getConsoleSize() {
        cursorSave();
        cursorTo(Integer.MAX_VALUE, Integer.MAX_VALUE);
        cursorRequest();
        cursorRestore();

        ConsolePosition pos = new ConsolePosition();

        try {
            StringBuilder sb = new StringBuilder();
            byte[] buff = new byte[1];
            while (System.in.read(buff, 0, 1) != -1) {
                sb.append((char) buff[0]);
                if (buff[0] == 'R') {
                    break;
                }
            }

            Matcher m = Pattern.compile("\\d+").matcher(sb.toString());
            List<String> regexMatches = new ArrayList<String>();

            for (int i = 0; i < 2; i++) {
                if (!m.find()) {
                    throw new IOException("Error when trying to get console size");
                }
                regexMatches.add(m.group());
            }

            pos.setRow(Integer.parseInt(regexMatches.get(0)));
            pos.setColumn(Integer.parseInt(regexMatches.get(1)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pos;
    }

    public static void scrollSet(int top, int bottom) {
        System.out.print(String.format("\033[%d;%dr", top, bottom));
    }

    public static void scrollUp() {
        System.out.print("\033D");
    }

    public static void scrollDown() {
        System.out.print("\033D");
    }

    public static void slowPrint(String s) {
        slowPrint(s, DEFAULT_SPEED);
    }

    public static void slowPrint(String s, int bps) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));

            if (bps == 0) {
                continue;
            }

            try {
                Thread.sleep((int) (8000.0 / bps));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
