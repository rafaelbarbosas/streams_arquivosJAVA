package br.com.letscode.util;

import java.util.Scanner;
import java.util.stream.Stream;

import br.com.letscode.exception.ExitSignalException;
import br.com.letscode.exception.GoBackSignalException;
import br.com.letscode.model.movie.Movie;
import br.com.letscode.model.system.ConsolePosition;
import br.com.letscode.model.system.Message;

public class SystemInterfaceUtils {
    public static final int DEFAULT_CONSOLE_WIDTH = 108;

    public static final String SYSTEM_LOGO = "  .-')                _   .-')    _   .-')                  .-')                                 .-') _   ('-.   "
            + ConsoleUtils.NEW_LINE
            + "( OO ).             ( '.( OO )_ ( '.( OO )_               ( OO ).                              ( OO ) )_(  OO)  "
            + ConsoleUtils.NEW_LINE
            + "(_)---\\_) .-'),-----. ,--.   ,--.),--.   ,--.),--. ,--.   (_)---\\_)         .-----.  ,-.-') ,--./ ,--,'(,------. "
            + ConsoleUtils.NEW_LINE
            + "/    _ | ( OO'  .-.  '|   `.'   | |   `.'   | |  | |  |   /    _ |         '  .--./  |  |OO)|   \\ |  |\\ |  .---' "
            + ConsoleUtils.NEW_LINE
            + "\\  :` `. /   |  | |  ||         | |         | |  | | .-') \\  :` `.         |  |('-.  |  |  \\|    \\|  | )|  |     "
            + ConsoleUtils.NEW_LINE
            + " '..`''.)\\_) |  |\\|  ||  |'.'|  | |  |'.'|  | |  |_|( OO ) '..`''.)       /_) |OO  ) |  |(_/|  .     |/(|  '--.  "
            + ConsoleUtils.NEW_LINE
            + ".-._)   \\  \\ |  | |  ||  |   |  | |  |   |  | |  | | `-' /.-._)   \\       ||  |`-'| ,|  |_.'|  |\\    |  |  .--'  "
            + ConsoleUtils.NEW_LINE
            + "\\       /   `'  '-'  '|  |   |  | |  |   |  |('  '-'(_.-' \\       /      (_'  '--'\\(_|  |   |  | \\   |  |  `---. "
            + ConsoleUtils.NEW_LINE
            + " `-----'      `-----' `--'   `--' `--'   `--'  `-----'     `-----'          `-----'  `--'   `--'  `--'  `------' ";

    public static final String WELCOME_STRING = " ____                            _           _                     "
            + ConsoleUtils.NEW_LINE
            + "| __ )  ___ _ __ ___      __   _(_)_ __   __| | ___     __ _  ___  " + ConsoleUtils.NEW_LINE
            + "|  _ \\ / _ \\ '_ ` _ \\ ____\\ \\ / / | '_ \\ / _` |/ _ \\   / _` |/ _ \\ " + ConsoleUtils.NEW_LINE
            + "| |_) |  __/ | | | | |_____\\ V /| | | | | (_| | (_) | | (_| | (_) |" + ConsoleUtils.NEW_LINE
            + "|____/ \\___|_| |_| |_|      \\_/ |_|_| |_|\\__,_|\\___/   \\__,_|\\___/ ";

    public static final int DEFAULT_LINES_PER_PAGE = 10;

    public static String getHeader(String screenName, ConsolePosition pos) {
        final String HEADER_START_TEXT = "Sommus Cine";
        final String time = TimeUtils.nowString();
        final int consoleMiddleRow = pos.getColumn() / 2;
        final int screenNameStartPadding = consoleMiddleRow - HEADER_START_TEXT.length() - (screenName.length() / 2);
        final int dateStartPadding = pos.getColumn() - screenNameStartPadding - time.length() - screenName.length()
                - HEADER_START_TEXT.length();

        return ConsoleUtils.Attribute.FCOL_PURPLE.getEscapeCode()
                + ConsoleUtils.Attribute.REVERSE.getEscapeCode()
                + HEADER_START_TEXT
                + StringUtils.blankSpaces(screenNameStartPadding)
                + screenName
                + StringUtils.blankSpaces(dateStartPadding)
                + time
                + ConsoleUtils.Attribute.RESET.getEscapeCode()
                + ConsoleUtils.NEW_LINE;
    }

    public static String getMessage(Message message, int consoleWidth) {
        String format = ConsoleUtils.Attribute.BRIGHT.getEscapeCode();

        String formatBGColor;
        switch (message.getType()) {
            case SUCCESS:
                formatBGColor = ConsoleUtils.Attribute.FCOL_GREEN.getEscapeCode();
                break;
            case ERROR:
                formatBGColor = ConsoleUtils.Attribute.FCOL_RED.getEscapeCode();
                break;
            case WARNING:
                formatBGColor = ConsoleUtils.Attribute.FCOL_YELLOW.getEscapeCode();
                break;
            case INFO:
                formatBGColor = ConsoleUtils.Attribute.FCOL_BLUE.getEscapeCode();
                break;
            default:
                formatBGColor = ConsoleUtils.Attribute.REVERSE.getEscapeCode();
                break;
        }
        format = format.concat(formatBGColor);

        return format
                + ConsoleUtils.Attribute.REVERSE.getEscapeCode()
                + StringUtils.centralize(message.getText(), consoleWidth)
                + StringUtils.blankSpaces((consoleWidth / 2) - (message.getText().length() / 2))
                + ConsoleUtils.Attribute.RESET.getEscapeCode()
                + ConsoleUtils.NEW_LINE;
    }

    public static void drawInfoScreen(String screenName, Message message, String content, ConsolePosition consoleSize) {
        ConsoleUtils.scrollScreen();

        System.out.print(SystemInterfaceUtils.getHeader(screenName, consoleSize));

        ConsoleUtils.skipLines(1);
        if (message.getText().length() > 0) {
            System.out.print(getMessage(message, consoleSize.getColumn()));
        } else {
            ConsoleUtils.skipLines(1);
        }

        ConsoleUtils.skipLines(1);

        System.out.print(content + ConsoleUtils.NEW_LINE);
        ConsoleUtils.skipLines(3);
    }

    public static void drawPaginationScreen(String screenName, Message message, String header, String content,
            ConsolePosition consoleSize, int currentPage, int totalPages) {
        ConsoleUtils.scrollScreen();

        System.out.print(SystemInterfaceUtils.getHeader(screenName, consoleSize));

        ConsoleUtils.skipLines(1);
        if (message.getText().length() > 0) {
            System.out.print(getMessage(message, consoleSize.getColumn()));
        } else {
            ConsoleUtils.skipLines(1);
        }

        ConsoleUtils.skipLines(1);

        System.out.print(header + ConsoleUtils.NEW_LINE);
        ConsoleUtils.skipLines(1);

        int linesPerPage = (int) Math.ceil((double) content.split("\n").length / (double) totalPages);
        int startLine = linesPerPage * (currentPage - 1);
        int endLine = linesPerPage * currentPage;
        if (endLine > content.split("\n").length) {
            endLine = content.split("\n").length;
        }
        String[] lines = content.split("\n");
        for (int i = startLine; i < endLine; i++) {
            System.out.print(lines[i] + ConsoleUtils.NEW_LINE);
        }

        ConsoleUtils.skipLines(1);
        System.out.print(StringUtils.centralize("Página " + currentPage + "/" + totalPages + ConsoleUtils.NEW_LINE,
                consoleSize.getColumn()));
    }

    private static String getMovieEntry(Movie movie) {
        return "#"
                + movie.getRank()
                + " "
                + movie.getTitle()
                + " - "
                + movie.getYear()
                + ", Rating: "
                + movie.getRating();
    }

    public static String getMoviesList(Stream<Movie> movies, int lineWidth) {
        return movies.map(movie -> StringUtils.centralize(getMovieEntry(movie), lineWidth))
                .reduce("",
                        (movieEntry, movieList) -> movieEntry.concat(ConsoleUtils.NEW_LINE).concat(movieList));
    }

    public static String getUserInput(Scanner scanner, ConsolePosition consoleSize, String message)
            throws ExitSignalException, GoBackSignalException {
        ConsoleUtils.cursorTo(consoleSize.getRow(), 1);
        System.out.print(ConsoleUtils.Attribute.REVERSE.getEscapeCode()
                + message
                + StringUtils.blankSpaces(consoleSize.getColumn() - message.length()));
        ConsoleUtils.cursorTo(consoleSize.getRow(), message.length() + 1);

        String userInput = scanner.nextLine();
        String userInputUpper = userInput.strip().toUpperCase();

        if (userInputUpper.equals("\\EXIT")) {
            throw new ExitSignalException("The user has sent the exit signal");
        }
        if (userInputUpper.equals("\\BACK") || userInputUpper.equals("\\B")) {
            throw new GoBackSignalException("The user has sent the go back signal");
        }

        return userInput;
    }
}
