package Game;

public class StaticBoard extends Board{

    public int Rows;
    public int Columns;

    public byte[][] board;

    public int getRader() {
        return Rows;
    }

    public void setRader(int rader) {
        Rows = rader;
    }

    public int getKolonner() {
        return Columns;
    }

    public void setKolonner(int kolonner) {
        Columns = kolonner;
    }

    public byte[][] getBoard() {
        return board;
    }

    public void setBoard(byte[][] board) {
        this.board = board;
    }
}
