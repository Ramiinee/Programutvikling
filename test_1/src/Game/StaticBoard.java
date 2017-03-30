package Game;

/**
 *
 * @author Joachim-Privat
 */
public class StaticBoard extends Board{

    /**
     *
     */
    public int Column;

    /**
     *
     */
    public int Row;

    /**
     *
     */
    public byte[][] board;

    /**
     *
     * @return
     */
    public int getRow() {
        return Row;
    }

    /**
     *
     * @param row
     */
    public void setRow(int row) {
        this.Row = row;
    }

    /**
     *
     * @return
     */
    public int getColumn() {
        return Column;
    }

    /**
     *
     * @param column
     */
    public void setColumn(int column) {
        this.Column = column;
    }

    /**
     *
     * @return
     */
    public byte[][] getBoard() {
        return board;
    }

    /**
     *
     * @param board
     */
    public void setBoard(byte[][] board) {
        this.board = board;
    }
}
