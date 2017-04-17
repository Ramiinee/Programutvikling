package Game;

/**
 *
 * @author Joachim-Privat
 */
public class StaticBoard extends Board{

    public int Column;
    public int Row;
    public byte[][] board;
    public byte[][] nextGeneration;

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

    public void noDeadCellsRule(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);

                if (board[col][row] == 0 && (neighbors == 3)) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }
        }
        board = nextGeneration;
    }

    public void nextGeneration(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);
                if((board[col][row] == 1) && (neighbors <  2)) {
                    nextGeneration[col][row] = 0;
                }
                else if ((board[col][row] == 1) && (neighbors >  3)) {
                    nextGeneration[col][row] = 0;
                }
                else if (board[col][row] == 0 && (neighbors == 3)) {
                    nextGeneration[col][row] = 1;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }

        }
        board = nextGeneration;
    }

    public void slowlyCover(){
        nextGeneration = new byte[board.length][board[0].length];
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                int neighbors = countNeighbor(col,row);
                if((board[col][row] == 1)) {
                    nextGeneration[col][row] = 1;
                }
                else if ((neighbors >  3)) {
                    nextGeneration[col][row] = 1;
                }
                else if ((neighbors == 3)) {
                    nextGeneration[col][row] = 0;
                }
                else {
                    nextGeneration[col][row] = board[col][row];
                }

            }

        }
        board = nextGeneration;
    }


    private int countNeighbor(int col, int row){
        int neighbors = 0;

        if (board[(col-1+board.length)%board.length][(row-1+board[row].length)%board[row].length] == 1){ neighbors++;}
        if (board[(col-1+board.length)%board.length][row] == 1){ neighbors++;}
        if (board[(col-1+board.length)%board.length][(row+1)%board[row].length] == 1){ neighbors++;}
        if (board[col][(row-1+board[row].length)%board[row].length] == 1){ neighbors++;}
        if (board[col][(row+1)%board[row].length] == 1){ neighbors++;}
        if (board[(col+1)%board.length][(row-1+board[row].length)%board[row].length] == 1){ neighbors++;}
        if (board[(col+1)%board.length][row] == 1){ neighbors++;}
        if (board[(col+1)%board.length][(row+1)%board[row].length] == 1){ neighbors++;}
        return neighbors;

    }
}
