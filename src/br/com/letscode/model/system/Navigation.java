package br.com.letscode.model.system;

import br.com.letscode.screens.ScreensList;

public class Navigation {
    private ScreensList screen;
    private String[] args;

    public Navigation() {
    }

    public Navigation(ScreensList screen, String[] args) {
        this.screen = screen;
        this.args = args;
    }

    public ScreensList getScreen() {
        return screen;
    }

    public void setScreen(ScreensList screen) {
        this.screen = screen;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return args[index];
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
