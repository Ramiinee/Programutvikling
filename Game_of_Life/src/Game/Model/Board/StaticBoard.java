package Game.model.Boards;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Joachim-Privat
 */
public class StaticBoard extends Board{

    private int Column;
    private int Row;
    public byte[][] board;
    private byte[][] nextGeneration;



    @Override
    public int getColumn() {
        return Column;
    }

    @Override
    public void setColumn(int column) {
        this.Column = column;
    }

    @Override
    public int getRow() {
        return board.length;
    }

    @Override
    public void setRow(int row) {
        this.Row = row;
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
        for (int row = 0; row <Row ; row++) {
            for (int col = 0; col <Column; col++) {
                int neighbors = countNeighbor(row, col);
                if ((board[row][col] == 1) && (neighbors < 2)) {
                    nextGeneration[row][col] = 0;
                } else if ((board[row][col] == 1) && (neighbors > 3)) {
                    nextGeneration[row][col] = 0;
                } else if (board[row][col] == 0 && (neighbors == 3)) {
                    nextGeneration[row][col] = 1;
                } else {
                    nextGeneration[row][col] = board[row][col];
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


    protected int countNeighbor(int col, int row){
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

    @Override
    public byte getCellAliveState(int row, int column) {
        if (row > getRow()-1 || row < 0 || column > getColumn()-1 || column < 0) {
            System.out.println("outside array");
            return 0;
        } else {
            return board[row][column];
        }
    }

    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        if (row > getRow()-1 || row < 0 || column > getColumn()-1 || column < 0) {

        } else {
            if (aliveState == 0 || aliveState == 1) {
                board[row][column] = aliveState;
            } else {
                throw new RuntimeException("Invalid number in cell state: " + aliveState);
            }
        }
    }



    @Override
    public void makeBoard(int row, int col) {
        this.Row = row;
        this.Column = col;
        board = new byte[Row][Column];
    }


}
