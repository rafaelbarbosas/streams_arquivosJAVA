package br.com.letscode.screens;

import java.util.NoSuchElementException;
import java.util.Scanner;

import br.com.letscode.model.system.ConsolePosition;
import br.com.letscode.model.system.Navigation;
import br.com.letscode.util.ConsoleUtils;
import br.com.letscode.util.StringUtils;
import br.com.letscode.util.SystemInterfaceUtils;

public class StartScreen implements ScreenInterface {
    public Navigation run(Scanner scanner, String[] args) {
        ConsoleUtils.clearScreen();
        ConsoleUtils.scrollScreen();

        System.out.print(
                StringUtils.addBlankSpacesToAllLines(SystemInterfaceUtils.WELCOME_STRING, 18) + ConsoleUtils.NEW_LINE);

        ConsoleUtils.skipLines(1);

        System.out.print(ConsoleUtils.Attribute.FCOL_PURPLE.getEscapeCode()
                + ConsoleUtils.Attribute.BLINK.getEscapeCode()
                + StringUtils.addBlankSpacesToAllLines(SystemInterfaceUtils.SYSTEM_LOGO, 1)
                + ConsoleUtils.NEW_LINE
                + ConsoleUtils.Attribute.RESET.getEscapeCode() + ConsoleUtils.NEW_LINE);

        System.out
                .print(StringUtils.centralize("Os arquivos de saída foram gerados",
                        SystemInterfaceUtils.DEFAULT_CONSOLE_WIDTH)
                        + ConsoleUtils.NEW_LINE
                        + StringUtils.centralize("Tecle ENTER caso deseje utilizar a interface gráfica",
                                SystemInterfaceUtils.DEFAULT_CONSOLE_WIDTH)
                        + ConsoleUtils.NEW_LINE
                        + StringUtils.centralize("Tecle CTRL + C caso deseje encerrar o programa",
                                SystemInterfaceUtils.DEFAULT_CONSOLE_WIDTH)
                        + ConsoleUtils.NEW_LINE
                        + StringUtils.centralize("Digite \"\\b\" ou \"\\back\" nas telas para retornar a tela anterior",
                                SystemInterfaceUtils.DEFAULT_CONSOLE_WIDTH)
                        + ConsoleUtils.NEW_LINE
                        + StringUtils.centralize("Digite \"\\exit\" nas telas para encerrar o programa",
                                SystemInterfaceUtils.DEFAULT_CONSOLE_WIDTH));

        System.out.print(ConsoleUtils.Attribute.FCOL_BLACK.getEscapeCode());
        ConsolePosition consolePos = ConsoleUtils.getConsoleSize();
        System.out.print(ConsoleUtils.Attribute.RESET.getEscapeCode());

        try {
            scanner.nextLine();
        } catch (NoSuchElementException e) {
            ConsoleUtils.clearScreen();
            return new Navigation(ScreensList.EXIT, args);
        }

        args = new String[2];
        args[0] = String.valueOf(consolePos.getRow());
        args[1] = String.valueOf(consolePos.getColumn());

        ConsoleUtils.clearScreen();
        return new Navigation(ScreensList.MAIN, args);
    }
}
