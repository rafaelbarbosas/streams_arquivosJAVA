package br.com.letscode.screens;

import java.util.Scanner;

import br.com.letscode.model.system.Navigation;
import br.com.letscode.util.ConsoleUtils;
import br.com.letscode.util.StringUtils;
import br.com.letscode.util.SystemInterfaceUtils;

public class ExitScreen implements ScreenInterface {
    private static final String EXIT_MESSAGE = "Muito obrigado por utilizar o Sommus Cine!!!";

    private static void draw() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.scrollScreen();

        System.out.print(ConsoleUtils.NEW_LINE
                + StringUtils.centralize(EXIT_MESSAGE,
                        SystemInterfaceUtils.DEFAULT_CONSOLE_WIDTH)
                + ConsoleUtils.NEW_LINE);

        ConsoleUtils.skipLines(1);

        System.out.print(ConsoleUtils.Attribute.FCOL_PURPLE.getEscapeCode()
                + ConsoleUtils.Attribute.BLINK.getEscapeCode()
                + StringUtils.addBlankSpacesToAllLines(SystemInterfaceUtils.SYSTEM_LOGO, 1)
                + ConsoleUtils.NEW_LINE
                + ConsoleUtils.Attribute.RESET.getEscapeCode() + ConsoleUtils.NEW_LINE);
    }

    public Navigation run(Scanner scanner, String[] args) {
        draw();

        return new Navigation(null, null);
    }
}
