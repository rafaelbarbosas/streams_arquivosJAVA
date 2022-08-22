package br.com.letscode.screens;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import br.com.letscode.dao.MovieDAO;
import br.com.letscode.exception.DatabaseException;
import br.com.letscode.exception.ExitSignalException;
import br.com.letscode.exception.GoBackSignalException;
import br.com.letscode.exception.InvalidCommandException;
import br.com.letscode.model.movie.Movie;
import br.com.letscode.model.system.ConsolePosition;
import br.com.letscode.model.system.Message;
import br.com.letscode.model.system.MessageType;
import br.com.letscode.model.system.Navigation;
import br.com.letscode.util.ConsoleUtils;
import br.com.letscode.util.StringUtils;
import br.com.letscode.util.SystemInterfaceUtils;

public class MainScreen implements ScreenInterface {
    private static final int HEADER_LINES = 10;

    private static void draw(
            ConsolePosition consoleSize,
            Message message,
            String screenContent,
            int currentPage,
            int totalPages) {

        final String SCREEN_NAME = "Sommus Cine";
        final String SCREEN_HEADER = StringUtils.centralize("########## Lista de filmes ##########",
                consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE
                + StringUtils.centralize("- Comandos disponiveis -", consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE
                + StringUtils.centralizeBlock("COMANDO {VARIAVEL} {VARIAVEL}"
                        + ConsoleUtils.NEW_LINE
                        + "COMANDO {VARIAVEL} {VARIAVEL}", consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE
                + StringUtils.centralize("Digite \\n ou \\p para navegar entre as páginas", consoleSize.getColumn())
                + StringUtils.multiply(ConsoleUtils.NEW_LINE, 2);

        SystemInterfaceUtils.drawPaginationScreen(SCREEN_NAME, message, SCREEN_HEADER, screenContent, consoleSize,
                currentPage, totalPages);
    }

    public Navigation executeUserCommand(
            String userCommand,
            int totalPages,
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

        String[] commandOperands = userCommand.strip().split(" ");

        return new Navigation(ScreensList.MAIN, args);
    }

    public Navigation run(Scanner scanner, String[] args) {
        ConsolePosition consoleSize = new ConsolePosition(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        Message message = new Message("", null);

        Set<Movie> moviesSet;
        try {
            moviesSet = MovieDAO.listAll();
        } catch (DatabaseException e) {
            message.setType(MessageType.ERROR);
            message.setText("Ocorreu um erro ao recuperar a lista de filmes");
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
            String screenContent = StringUtils.centralizeBlock(
                    "Screen Content",
                    consoleSize.getColumn());
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
                return new Navigation(ScreensList.MAIN, returnArgs);
            } catch (NoSuchElementException e) {
                // do nothing
            }
            System.out.print(ConsoleUtils.Attribute.RESET.getEscapeCode());

            try {
                Navigation commandReturn = executeUserCommand(userInput, totalPages, args);
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
