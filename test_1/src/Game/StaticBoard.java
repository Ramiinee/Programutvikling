package Game;

public class StaticBoard extends Board{

    public int Rader;
    public int Kolonner;

    public byte[][] board;

    public int getRader() {
        return Rader;
    }

    public void setRader(int rader) {
        Rader = rader;
    }

    public int getKolonner() {
        return Kolonner;
    }

    public void setKolonner(int kolonner) {
        Kolonner = kolonner;
    }

    public byte[][] getBoard() {
        return board;
    }

    public void setBoard(byte[][] board) {
        this.board = board;
    }
}
