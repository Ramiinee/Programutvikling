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
    public byte[][] board;
    /**
     * NextGeneration styrer hvordan neste genereasjon skal se ut. Med hjelp av countNeighbor så settes neste generasjons
     * brett til rett verdi.
     */
    public void nextGeneration(){
       byte[][] nextGeneration = new byte[board.length][board[0].length];
        for (int col = 1; col < board.length-1; col++) {
            for (int row = 1; row < board[col].length -1 ; row++) {
                int neighbors = countNeighbor(col,row);
                if(neighbors <  2) {
                    nextGeneration[col][row] = 0;
                }
                else if (neighbors >  3) {
                    nextGeneration[col][row] = 0;
                }
                else if (neighbors == 3) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }
            }
        }
        board = nextGeneration;
    }

    /**
     * countNeighbor avgjør for hver enkelt celle om den skal få leve eller ikke. Reglene styres her inne.
     * @param col
     * @param row
     * row and col controll the position of the cell.
     * @return amount of neighbors per cell
     */
    protected int countNeighbor(int col, int row){
        int neighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                neighbors += board[col+i][row+j];
            }
        }
        neighbors -= board[col][row];
        return neighbors;
    }
}
