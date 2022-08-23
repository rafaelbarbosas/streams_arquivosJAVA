package br.com.letscode.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.letscode.App;
import br.com.letscode.dao.MovieDAO;
import br.com.letscode.dao.MovieManager;
import br.com.letscode.exception.DatabaseException;
import br.com.letscode.exception.ExitSignalException;
import br.com.letscode.exception.GoBackSignalException;
import br.com.letscode.exception.InvalidCommandException;
import br.com.letscode.model.movie.Movie;
import br.com.letscode.model.movie.MoviesFilePropertiesEnum;
import br.com.letscode.model.system.Conditional;
import br.com.letscode.model.system.ConsolePosition;
import br.com.letscode.model.system.Message;
import br.com.letscode.model.system.MessageType;
import br.com.letscode.model.system.Navigation;
import br.com.letscode.util.ConsoleUtils;
import br.com.letscode.util.FileUtils;
import br.com.letscode.util.StringUtils;
import br.com.letscode.util.SystemInterfaceUtils;

public class MainScreen implements ScreenInterface {
    private static final int HEADER_LINES = 8;

    private static final String COMMAND_SEPARATOR_REGEX = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    private static void draw(
            ConsolePosition consoleSize,
            Message message,
            String screenContent,
            int currentPage,
            int totalPages) {

        final String SCREEN_NAME = "Cartaz";
        final String SCREEN_HEADER = StringUtils.centralize("########## Lista de filmes ##########",
                consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE
                + StringUtils.centralize("- Comandos disponiveis -", consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE
                + StringUtils.centralizeBlock("FILTER {PROPRIEDADE} {COMPARADOR} {VALOR}"
                        + ConsoleUtils.NEW_LINE
                        + "SORT {PROPRIEDADE} [REVERSE]?"
                        + ConsoleUtils.NEW_LINE
                        + "LIMIT {QUANTIDADE}"
                        + ConsoleUtils.NEW_LINE
                        + "SKIP {QUANTIDADE}"
                        + ConsoleUtils.NEW_LINE
                        + "SAVE {NOME_ARQUIVO}"
                        + ConsoleUtils.NEW_LINE
                        + "RESET", consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE
                + StringUtils.centralize("Digite \\n ou \\p para navegar entre as páginas", consoleSize.getColumn());

        SystemInterfaceUtils.drawPaginationScreen(SCREEN_NAME, message, SCREEN_HEADER, screenContent, consoleSize,
                currentPage, totalPages);
    }

    public Navigation executeUserCommand(
            String userCommand,
            int totalPages,
            List<Movie> moviesList,
            String[] args)
            throws InvalidCommandException {

        int page = Integer.parseInt(args[2]);

        if (userCommand.toLowerCase().equals("\\n")) {
            if (page >= totalPages) {
                throw new InvalidCommandException("Não há mais páginas");
            }
            args = StringUtils.removeArgFromList(args, 2);
            return new Navigation(ScreensList.MAIN, StringUtils.addArgToList(args, String.valueOf(page + 1)));
        }
        if (userCommand.toLowerCase().equals("\\p")) {
            if (page <= 1) {
                throw new InvalidCommandException("Não há mais páginas");
            }
            args = StringUtils.removeArgFromList(args, 2);
            return new Navigation(ScreensList.MAIN, StringUtils.addArgToList(args, String.valueOf(page - 1)));
        }

        String[] commandOperands = userCommand.strip().split(COMMAND_SEPARATOR_REGEX, -1);
        System.out.println(commandOperands.toString());
        MoviesFilePropertiesEnum property;

        try {
            switch (commandOperands.length) {
                case 1:
                    if (!commandOperands[0].toUpperCase().equals("RESET")) {
                        throw new InvalidCommandException("Comando inválido!");
                    }
                    try {
                        moviesList.clear();
                        moviesList.addAll(MovieDAO.listAll().stream().sorted().collect(Collectors.toList()));
                    } catch (DatabaseException e) {
                        throw new InvalidCommandException("Ocorreu um erro ao recuperar a lista de filmes");
                    }
                    break;
                case 2:
                    switch (commandOperands[0].toUpperCase()) {
                        case "LIMIT":
                            moviesList.retainAll(moviesList.stream()
                                    .limit(Integer.parseInt(commandOperands[1]))
                                    .collect(Collectors.toList()));
                            break;
                        case "SKIP":
                            moviesList.retainAll(moviesList.stream()
                                    .skip(Integer.parseInt(commandOperands[1]))
                                    .collect(Collectors.toList()));
                            break;
                        case "SAVE":
                            MovieManager.writeMovieStreamToFile(
                                    moviesList.stream(),
                                    FileUtils.getFullFilePath(App.OUTPUT_FOLDER_PATH, commandOperands[1]),
                                    App.FILES_CHAR_SET,
                                    true);
                            break;
                        case "SORT":
                            property = MoviesFilePropertiesEnum
                                    .valueOf(commandOperands[1].toUpperCase());
                            moviesList.sort(property.getPropertyComparator());
                            break;
                        default:
                            throw new InvalidCommandException("Comando inválido!");
                    }
                    break;
                case 3:
                    if (!commandOperands[0].toUpperCase().equals("SORT")
                            || !commandOperands[2].toUpperCase().equals("REVERSE")) {
                        throw new InvalidCommandException("Comando inválido!");
                    }
                    property = MoviesFilePropertiesEnum.valueOf(commandOperands[1].toUpperCase());
                    moviesList.sort(property.getPropertyComparator());
                    Collections.reverse(moviesList);
                    break;
                case 4:
                    if (!commandOperands[0].toUpperCase().equals("FILTER")) {
                        throw new InvalidCommandException("Comando inválido!");
                    }
                    property = MoviesFilePropertiesEnum.valueOf(commandOperands[1].toUpperCase());
                    Conditional conditional = Conditional.valueOfSymbol(commandOperands[2].toUpperCase());
                    if (conditional == null) {
                        throw new InvalidCommandException("Comparador inválido!");
                    }
                    String condition = commandOperands[3].toUpperCase();
                    moviesList.retainAll(
                            moviesList.stream()
                                    .filter(movie -> property.testFilter(movie, conditional, condition))
                                    .collect(Collectors.toSet()));
                    break;
                default:
                    throw new InvalidCommandException("Comando inválido!");
            }
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Número inválido!");
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Propriedade inválida!");
        }

        return new Navigation(ScreensList.MAIN, args);
    }

    public Navigation run(Scanner scanner, String[] args) {
        ConsolePosition consoleSize = new ConsolePosition(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        Message message = new Message("", null);

        List<Movie> moviesList;
        try {
            moviesList = new ArrayList<>(MovieDAO.listAll().stream().sorted().collect(Collectors.toList()));
        } catch (DatabaseException e) {
            message.setType(MessageType.ERROR);
            message.setText("Ocorreu um erro ao recuperar a lista de filmes");
            moviesList = new ArrayList<>();
        }

        int totalPages;
        int page;
        try {
            page = Integer.parseInt(args[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            page = 1;
            args = StringUtils.addArgToList(args, String.valueOf(page));
        }

        while (true) {
            String screenContent = SystemInterfaceUtils.getMoviesList(moviesList.stream(), consoleSize.getColumn());
            totalPages = (int) Math.ceil((double) screenContent.split("\n").length
                    / ((double) consoleSize.getRow() - (double) SystemInterfaceUtils.DEFAULT_LINES_PER_PAGE
                            - (double) HEADER_LINES));

            ConsoleUtils.clearScreen();
            draw(consoleSize, message, screenContent, page, totalPages);

            String promptMessage = "Digite o comando que deseja executar: ";
            String userInput = "";
            try {
                userInput = SystemInterfaceUtils.getUserInput(scanner, consoleSize, promptMessage).strip();
            } catch (ExitSignalException e) {
                ConsoleUtils.clearScreen();
                return new Navigation(ScreensList.EXIT, args);
            } catch (GoBackSignalException e) {
                ConsoleUtils.clearScreen();
                String[] returnArgs = { args[0], args[1] };
                return new Navigation(ScreensList.START, returnArgs);
            } catch (NoSuchElementException e) {
                // do nothing
            }
            System.out.print(ConsoleUtils.Attribute.RESET.getEscapeCode());

            try {
                Navigation commandReturn = executeUserCommand(userInput, totalPages, moviesList, args);
                if (commandReturn.getScreen() != ScreensList.MAIN) {
                    ConsoleUtils.clearScreen();
                    return commandReturn;
                }
                page = Integer.parseInt(commandReturn.getArg(2));
                args = StringUtils.removeArgFromList(args, 2);
                args = StringUtils.addArgToList(args, String.valueOf(page));

                message.setText("Comando executado com sucesso!");
                message.setType(MessageType.SUCCESS);
                continue;
            } catch (InvalidCommandException e) {
                message.setText(e.getMessage());
                message.setType(MessageType.ERROR);
                continue;
            }
        }
    }

}
