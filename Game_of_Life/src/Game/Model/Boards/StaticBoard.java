package Game.Model.Boards;

import java.util.Arrays;
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
    private byte[][] neighborArray;
    private byte[][] neighborArrayold;
    private boolean t = true;


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
        return Row;
    }

    @Override
    public void setRow(int row) {
        this.Row = row;
    }

    public void noDeadCellsRule(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int col = start; col < stop; col++) {
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


        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }


    }
    @Override
    public void setBoard(){
        board = nextGeneration;
        nextGeneration = null;
    }

    @Override
    public void makeNextGenArray() {

        nextGeneration = new byte[board.length][board[0].length];
        if (t){

            neighborArrayold = new byte[board.length+2][board[0].length+3];
            for (int i = 0; i <neighborArrayold.length ; i++) {
                for (int y = 0; y < neighborArrayold[i].length; y++) {
                    neighborArrayold[i][y] = 1;
                }
            }
            System.out.println(t);
            t = false;
            neighborArray = neighborArrayold;
        }else {
            neighborArrayold = neighborArray;
            neighborArray = new byte[board.length+2][board[0].length+3];
        }
    }

    @Override
    public void test() {
        System.out.println();
        for (int i = 0; i < neighborArrayold.length; i++) {
            for (int i1 = 0; i1 < neighborArrayold[i].length; i1++) {
                System.out.print(neighborArrayold[i][i1]);
            }
            System.out.println();
        }
    }


    public void nextGeneration(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int row = start; row < stop ; row++) {
            for (int col = 0; col < board[row].length; col++) {
                //if (neighborArrayold[row][col] == 0 && board[row][col]==0) continue;

                int neighbors = countNeighbor(row, col);
                if ((board[row][col] == 1) && (neighbors < 2)) {
                    nextGeneration[row][col] = 0;
                } else if ((board[row][col] == 1) && (neighbors > 3)) {
                    nextGeneration[row][col] = 0;
                } else if (board[row][col] == 0 && (neighbors == 3)) {
                    nextGeneration[row][col] = 1;
                    setNeighborArray(row,col);
                    neighborArray[row][col] = 1;
                } else {
                    nextGeneration[row][col] = board[row][col];
                    if (nextGeneration[row][col] == 1){
                        setNeighborArray(row,col);
                        neighborArray[row][col] = 1;
                    }
                }
            }
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();
        }
    }

    private void setNeighborArray(int row, int col){

        row++;
        col++;
        try {
            neighborArray[row + 1][col] = 1;
        }catch (Exception e){

        }

        try {
            neighborArray[row + 1][col + 1] = 1;
        }catch (Exception e){

        }
        try {
            neighborArray[row][col + 1] = 1;
        }catch (Exception e){

        }
        try {
            neighborArray[row - 1][col + 1]= 1;
        }catch (Exception e){

        }
        try {
            neighborArray[row - 1][col] = 1;
        }catch (Exception e){

        }
        try {
            neighborArray[row - 1][col - 1] = 1;
        }catch (Exception e){

        }
        try {
            neighborArray[row][col - 1] = 1;
        }catch (Exception e){

        }
        try {
            neighborArray[row +1][col - 1] = 1;
        }catch (Exception e){

        }





    }

    public void slowlyCover(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int row = start; row < stop ; row++) {
            for (int col = 0; col < board[row].length; col++) {
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
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Main Thread interrupted!");
            e.printStackTrace();

        }
    }


    protected int countNeighbor(int row, int col){
        int neighbors = 0;
        // Check cell on the right.
        if (row != board.length - 1)
            if (board[row + 1][col] == 1)
                neighbors++;

        // Check cell on the bottom right.
        if (row != board.length - 1 && col != board[row].length - 1)
            if (board[row + 1][col + 1] == 1)
                neighbors++;

        // Check cell on the bottom.
        if (col != board[row].length - 1)
            if (board[row][col + 1] == 1)
                neighbors++;

        // Check cell on the bottom left.
        if (row != 0 && col != board[row].length - 1)
            if (board[row - 1][col + 1]== 1)
                neighbors++;

        // Check cell on the left.
        if (row != 0)
            if (board[row - 1][col] == 1)
                neighbors++;

        // Check cell on the top left.
        if (row != 0 && col != 0)
            if (board[row - 1][col - 1] == 1)
                neighbors++;

        // Check cell on the top.
        if (col != 0)
            if (board[row][col - 1] == 1)
                neighbors++;

        // Check cell on the top right.
        if (row != board.length - 1 && col != 0)
            if (board[row +1][col - 1] == 1)
                neighbors++;

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
        if (!(row > getRow()-1 || row < 0 || column > getColumn()-1 || column < 0)) {
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

    @Override
    public void makeBoard(byte[][] byteboard) {
        this.Row = byteboard.length;
        this.Column = byteboard[0].length;
        board = byteboard;
    }


}
