package Game;

public class StaticBoard extends Board{

    public int Column;
    public int Row;

    public byte[][] board;


    public int getRow() {
        return Row;
    }

    public void setRow(int row) {
        this.Row = row;
    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        this.Column = column;
    }

    public byte[][] getBoard() {
        return board;
    }

    public void setBoard(byte[][] board) {
        this.board = board;
    }
}
