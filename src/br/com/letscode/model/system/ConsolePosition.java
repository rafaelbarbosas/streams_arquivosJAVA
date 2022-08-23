package br.com.letscode.model.system;

public class ConsolePosition {
    private int row;
    private int column;

    public ConsolePosition() {
    }

    public ConsolePosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
