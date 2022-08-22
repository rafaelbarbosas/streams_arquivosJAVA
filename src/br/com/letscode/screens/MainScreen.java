package br.com.letscode.screens;

import java.util.NoSuchElementException;
import java.util.Scanner;

import br.com.letscode.exception.ExitSignalException;
import br.com.letscode.exception.GoBackSignalException;
import br.com.letscode.model.system.ConsolePosition;
import br.com.letscode.model.system.Message;
import br.com.letscode.model.system.MessageType;
import br.com.letscode.model.system.Navigation;
import br.com.letscode.util.ConsoleUtils;
import br.com.letscode.util.StringUtils;
import br.com.letscode.util.SystemInterfaceUtils;

public class MainScreen implements ScreenInterface {
    private static void draw(ConsolePosition consoleSize, Message message) {
        final String SCREEN_NAME = "Home";
        final String SCREEN_CONTENT = StringUtils.centralize("########## Digite a opção desejada: ##########",
                consoleSize.getColumn())
                + StringUtils.multiply(ConsoleUtils.NEW_LINE, 4)
                + StringUtils.centralizeBlock("1. Iniciar sessão de compras"
                        + ConsoleUtils.NEW_LINE
                        + "2. Listar produtos"
                        + ConsoleUtils.NEW_LINE
                        + "3. Cadastrar produto"
                        + ConsoleUtils.NEW_LINE
                        + "4. Listar tipos de produto"
                        + ConsoleUtils.NEW_LINE,
                        consoleSize.getColumn())
                + ConsoleUtils.NEW_LINE;

        SystemInterfaceUtils.drawInfoScreen(SCREEN_NAME, message, SCREEN_CONTENT, consoleSize);
    }

    public Navigation run(Scanner scanner, String[] args) {
        ConsolePosition consoleSize = new ConsolePosition(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        Message message = new Message("", MessageType.ERROR);
        Navigation navigate = new Navigation();

        while (true) {
            ConsoleUtils.clearScreen();
            draw(consoleSize, message);
            int userInput = 0;
            try {
                userInput = Integer.parseInt(
                        SystemInterfaceUtils.getUserInput(scanner, consoleSize, "Digite o número da opção desejada: "));
            } catch (NumberFormatException e) {
                message.setText("Opção inválida!");
                continue;
            } catch (ExitSignalException e) {
                ConsoleUtils.clearScreen();
                return new Navigation(ScreensList.EXIT, args);
            } catch (GoBackSignalException e) {
                ConsoleUtils.clearScreen();
                return new Navigation(ScreensList.START, args);
            } catch (NoSuchElementException e) {
                // do nothing
            }

            System.out.print(ConsoleUtils.Attribute.RESET.getEscapeCode());

            switch (userInput) {
                case 1:
                    navigate.setScreen(ScreensList.EXIT);
                    break;
                default:
                    navigate.setScreen(ScreensList.EXIT);
                    message.setText("Opção inválida!");
                    continue;
            }
            break;
        }

        ConsoleUtils.clearScreen();
        navigate.setArgs(args);
        return navigate;
    }
}
