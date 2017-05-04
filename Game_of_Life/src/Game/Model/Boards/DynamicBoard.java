package Game.Model.Boards;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class DynamicBoard extends Board{

    private int MIN_ROW;
    private int MIN_COL;

    public List<List<Byte>> board;
    private List<List<Byte>> nextGeneration;





    public int getRow() {
        return board.size();
    }
    public void setRow(int row) {
        this.MIN_ROW = row;
    }
    public int getColumn() {
        return board.get(0).size();
    }
    public void setColumn(int column) {
        this.MIN_COL = column;
    }

    public void nextGeneration(int start, int stop, CyclicBarrier cyclicBarrier){

        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {

                int neighbors = countNeighbor(col,row);

                if (board.get(row).get(col)==1 && (neighbors < 2)){
                    nextGeneration.get(row).set(col,(byte)0);
                }
                else if (board.get(row).get(col) == 1 && (neighbors > 3)) {
                    nextGeneration.get(row).set(col,(byte)0);
                }
                else if ( board.get(row).get(col) == 0 && (neighbors == 3)){
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else {
                    nextGeneration.get(row).set(col, board.get(row).get(col));
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

    public void slowlyCover(int start, int stop, CyclicBarrier cyclicBarrier){


        for (int row = start; row < stop ; row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(col,row);
                if (board.get(row).get(col)==1){
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else if (neighbors > 3) {
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else if (neighbors == 3){
                    nextGeneration.get(row).set(col,(byte)0);
                }
                else {
                    nextGeneration.get(row).set(col, board.get(row).get(col));
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

    public void noDeadCellsRule(int start, int slutt, CyclicBarrier cyclicBarrier){
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                int neighbors = countNeighbor(col,row);
                if ( board.get(row).get(col) == 0 && (neighbors == 3)){
                    nextGeneration.get(row).set(col,(byte)1);
                }
                else {
                    nextGeneration.get(row).set(col, board.get(row).get(col));
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
        setCurrentGen();


    }

    @Override
    public void makeNextGenArray() {
        expand();
        nextGeneration =  new ArrayList<>();
        for (int row = 0; row < board.size(); row++) {
            nextGeneration.add(new java.util.ArrayList<>());
            for (int col = 0; col < board.get(row).size(); col++) {
                nextGeneration.get(row).add((byte) 0);
            }
        }
    }

    @Override
    public void test() {

    }


    private void setCurrentGen(){
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.get(row).size(); col++) {
                byte a = nextGeneration.get(row).get(col);
                board.get(row).set(col,a);

            }
        }
        nextGeneration.clear();
    }

    protected int countNeighbor(int col, int row){
        int neighbors = 0;
        // Check cell on the right.
        if (row != board.size() - 1)
            if (board.get(row + 1).get(col) == 1)
                neighbors++;

        // Check cell on the bottom right.
        if (row != board.size() - 1 && col != board.get(row).size() - 1)
            if (board.get(row + 1).get(col + 1) == 1)
                neighbors++;

        // Check cell on the bottom.
        if (col != board.get(row).size() - 1)
            if (board.get(row).get(col + 1) == 1)
                neighbors++;

        // Check cell on the bottom left.
        if (row != 0 && col != board.get(row).size() - 1)
            if (board.get(row - 1).get(col + 1)== 1)
                neighbors++;

        // Check cell on the left.
        if (row != 0)
            if (board.get(row - 1).get(col) == 1)
                neighbors++;

        // Check cell on the top left.
        if (row != 0 && col != 0)
            if (board.get(row - 1).get(col - 1) == 1)
                neighbors++;

        // Check cell on the top.
        if (col != 0)
            if (board.get(row).get(col - 1) == 1)
                neighbors++;

        // Check cell on the top right.
        if (row != board.size() - 1 && col != 0)
            if (board.get(row + 1).get(col - 1) == 1)
                neighbors++;

        return neighbors;
    }
    private void expand(){
        // the struggle is real.
        //board.get(row).get(col)
        int maxRow = board.size()-1;
        int maxCol = board.get(maxRow).size() -1;

        for (int top = 0; top < board.get(0).size(); top++) {

            if(board.get(0).get(top) == 1){
                addTopRow(1);
                break;
            }
        }

        for (List<Byte> aBoard : board) {
            if (aBoard.get(0) == 1) {
                addLeftColumn(1);
                break;
            }
        }

        for (List<Byte> aBoard : board) {
            if (aBoard.get(maxCol) == 1) {
                addRightColumn(1);
                break;
            }
        }

        for( int bottom = 0; bottom < board.get(maxRow).size(); bottom++){
            if(board.get(maxRow).get(bottom) == 1){
                addBottomRow(1);
                break;
            }
        }
    }





    private void addTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.add(0, new ArrayList<>());
            for (int col = 0; col < board.get(board.size() - 1).size(); col++) {
                board.get(0).add((byte) 0);
            }
        }
    }

    private void addRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.forEach((col) -> col.add((byte) 0));
        }
    }
    private void addLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.forEach((col) -> col.add(0, (byte) 0));
        }
    }
    private void addBottomRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.add(new ArrayList<>());
            for (int col = 0; col < board.get(0).size(); col++) {
                board.get(board.size() - 1).add((byte) 0);
            }
        }
    }
    private void removeTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.remove(0);
        }
    }
    private void removeRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : board) {
                row.remove(board.get(0).size() - 1);
            }
        }
    }


    // rle leser inn i brett. Kan bestemme hvor så du kan ha flere figurer samtidig.
    // if white set backgrunfd to black.


    @Override
    public byte getCellAliveState(int row, int column) {
        if (row > getRow() - 1 || row < 0 || column > getColumn() - 1 || column < 0) {
            return (byte)0;
        } else {
            return board.get(row).get(column);
        }
    }

    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        this.board.get(row).set(column, aliveState);
    }



    @Override
    public void makeBoard(int row, int col) {
        this.MIN_ROW = row;
        this.MIN_COL = col;

        board = new ArrayList<>();

        for (int rows = 0; rows < MIN_ROW; rows++) {
            board.add(new java.util.ArrayList<>());
            for (int cols = 0; cols < MIN_COL; cols++) {
                board.get(rows).add((byte) 0);

            }
        }



    }

    @Override
    public void makeBoard(byte[][] byteboard) {
        this.MIN_ROW = byteboard.length;
        this.MIN_COL = byteboard[0].length;

        board = new ArrayList<>();

        for (int rows = 0; rows < MIN_ROW; rows++) {
            board.add(new java.util.ArrayList<>());
            for (int cols = 0; cols < MIN_COL; cols++) {
                board.get(rows).add(byteboard[rows][cols]);

            }
        }
    }


}
