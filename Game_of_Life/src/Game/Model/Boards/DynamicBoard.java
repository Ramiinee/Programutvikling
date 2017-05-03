package Game.Model.Boards;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.list;
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
        //addTopRow(10);


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
    public void expand(){

        //board.get(row).get(col)
        
        checkTop();
        checkLeft();
        checkRight();
        checkBottom();
        

    }

    public void checkTop(){
        
        int count = 0;

        for (int topadd = 0; topadd < board.get(0).size(); topadd++) {
            for (int topdelete = 0; topdelete < board.get(0).size(); topdelete++) {
                if(board.get(0).get(topdelete) == 1){
                    count ++; 
                }
            }
            if(count == 0){
            removeTopRow(1);
            }
            if(board.get(0).get(topadd) == 1){
                addTopRow(1);
                return;
            }
        }
    }
    
    public void checkLeft(){
        
        int count = 0;
           
       
        for(int leftadd = 0; leftadd < board.size(); leftadd ++){
             for(int leftdelete = 0; leftdelete < board.size(); leftdelete ++){
            if(board.get(leftdelete).get(0) == 1){
                count++;
            }
            }
             if(count == 0){
             removeLeftColumn(1);
             
             }
       
            if(board.get(leftadd).get(0) == 1){
                addLeftColumn(1);
                return;
            }
        }
    }
   
    
    public void checkRight(){
        int sum1 = 0;
        int sum2 = 0;

   

        final int columns = board.get(0).size();

        for (List<Byte> e : board) {
            sum1 += e.get(columns - 1);
            sum2 += e.get(columns - 2);

           

        }
        final int remove = sum1 + sum2;
        final int add = sum1 + sum2;

        if (add != 0) {
            addRightColumn(1);
        } else if (remove == 0 && board.get(0).size() > MIN_COL){
            removeRightColumn(1);
        }
    }
    
    
    
  
    
    public void checkBottom(){
        int maxRow = board.size()-1;
        int maxCol = board.get(maxRow).size() -1;
        // delete
        int count = 0;
          
        // add
        for( int bottomadd = 0; bottomadd < board.get(maxRow).size(); bottomadd++){
              for( int bottomdelete = 0; bottomdelete < board.get(maxRow).size(); bottomdelete++){
                if(board.get(maxRow).get(bottomdelete) == 1){
                count++;
            }
              }
              if(count == 0){
              removeBottomRow(1);
              }
            if(board.get(maxRow).get(bottomadd) == 1){
                addBottomRow(1);
                return;
            }
        }
      
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
            board.stream().forEach((col) -> col.add((byte) 0));
        }
    }
    private void addLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            board.stream().forEach((col) -> col.add(0, (byte) 0));
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
    private void removeLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : board) {
                row.remove(0);
            }
        }
    }
    private void removeRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : board) {
                row.remove(board.get(0).size() - 1);
            }
        }
    }
    private void removeBottomRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            board.remove(board.size() - 1);
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
    public void nextGeneration(int start, int stop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




}
