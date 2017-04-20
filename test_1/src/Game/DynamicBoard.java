package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DynamicBoard extends Board{

    private int MIN_ROW;
    private int MIN_COL;
    private List<List<Byte>> currentGeneration;
    private List<List<Byte>> nextGeneration;


    public DynamicBoard(int Row,int Col){
        this.MIN_COL = Col;
        this.MIN_ROW = Row;

        currentGeneration = new ArrayList<>();


        for (int row = 0; row < MIN_ROW; row++) {
            currentGeneration.add(new java.util.ArrayList<>());
            for (int col = 0; col < MIN_COL; col++) {
                currentGeneration.get(row).add((byte) 1);

            }
        }
        random();


    }
    public void random(){
        Random r = new Random();
        for (int row = 0; row < MIN_ROW; row++) {
            for (int col = 0; col < MIN_COL; col++) {
                int a = r.nextInt(2);
                if ( a== 0){
                    currentGeneration.get(row).set(col,(byte) 1);
                }else {
                    currentGeneration.get(row).set(col,(byte) 0);
                }


            }
        }
    }

    public void editValueArray(int row, int col,byte value){
        this.currentGeneration.get(row).set(col,value);
    }
    public void printBoard(){

        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(row).size(); col++) {
                System.out.print(currentGeneration.get(row).get(col));
            }
            System.out.println();
        }
    }
    

    public List<List<Byte>> getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(List<List<Byte>> currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public int getRow() {
        return currentGeneration.size();
    }

    
    public void setRow(int row) {
        this.MIN_ROW = row;
    }

    public int getColumn() {
        return currentGeneration.get(0).size();
    }

    
    public void setColumn(int column) {
        this.MIN_COL = column;
    }

    public void nextGeneration(){
        makeNextGen();
        
        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(row).size(); col++) {

                int neighbors = countNeighbor(col,row);
                
                
               if (currentGeneration.get(row).get(col)==1 && (neighbors < 2)){
                   nextGeneration.get(row).set(col,(byte)0);
               }
               else if (currentGeneration.get(row).get(col) == 1 && (neighbors > 3)) {
                   nextGeneration.get(row).set(col,(byte)0);

               }
               else if ( currentGeneration.get(row).get(col) == 0 && (neighbors == 3)){
                   nextGeneration.get(row).set(col,(byte)1);
               }
               else {
                   nextGeneration.get(row).set(col, currentGeneration.get(row).get(col));
               }

            }
                 
            }

        

        setCurrentGen();
    }
    
    public void slowlyCover(){
        makeNextGen();
        
        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(row).size(); col++) {

                int neighbors = countNeighbor(col,row);
                
                
               if (currentGeneration.get(row).get(col)==1){
                   nextGeneration.get(row).set(col,(byte)1);
               }
               else if (neighbors > 3) {
                   nextGeneration.get(row).set(col,(byte)1);

               }
               else if (neighbors == 3){
                   nextGeneration.get(row).set(col,(byte)0);
               }
               else {
                   nextGeneration.get(row).set(col, currentGeneration.get(row).get(col));
               }

            }
                 
            }

        

        setCurrentGen();
    }
    
     public void noDeadCellsRule(){
        makeNextGen();
        
        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(row).size(); col++) {

                int neighbors = countNeighbor(col,row);

               if ( currentGeneration.get(row).get(col) == 0 && (neighbors == 3)){
                   nextGeneration.get(row).set(col,(byte)1);
               }
               else {
                   nextGeneration.get(row).set(col, currentGeneration.get(row).get(col));
               }

            }
                 
            }

        

        setCurrentGen();
    }
    
    
    
    
    
    private int countNeighbor(int col, int row){
        int neighbors = 0;
// Check cell on the right.
    if (row != currentGeneration.size() - 1)
        if (currentGeneration.get(row + 1).get(col) == 1)
            neighbors++;
 
    // Check cell on the bottom right.
    if (row != currentGeneration.size() - 1 && col != currentGeneration.get(row).size() - 1)
        if (currentGeneration.get(row + 1).get(col + 1) == 1)
            neighbors++;
 
    // Check cell on the bottom.
    if (col != currentGeneration.get(row).size() - 1)
        if (currentGeneration.get(row).get(col + 1) == 1)
            neighbors++;
 
    // Check cell on the bottom left.
    if (row != 0 && col != currentGeneration.get(row).size() - 1)
        if (currentGeneration.get(row - 1).get(col + 1)== 1)
            neighbors++;
 
    // Check cell on the left.
    if (row != 0)
        if (currentGeneration.get(row - 1).get(col) == 1)
            neighbors++;
 
    // Check cell on the top left.
    if (row != 0 && col != 0)
        if (currentGeneration.get(row - 1).get(col - 1) == 1)
            neighbors++;
 
    // Check cell on the top.
    if (col != 0)
        if (currentGeneration.get(row).get(col - 1) == 1)
            neighbors++;
 
    // Check cell on the top right.
    if (row != currentGeneration.size() - 1 && col != 0)
        if (currentGeneration.get(row + 1).get(col - 1) == 1)
            neighbors++;

    return neighbors;
    }

    private void setCurrentGen(){
        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(row).size(); col++) {
                byte a = nextGeneration.get(row).get(col);
                currentGeneration.get(row).set(col,a);

            }
        }
        nextGeneration.clear();
    }
    private void makeNextGen(){
        nextGeneration =  new ArrayList<>();
        for (int row = 0; row < currentGeneration.size(); row++) {
            nextGeneration.add(new java.util.ArrayList<>());
            for (int col = 0; col < currentGeneration.get(row).size(); col++) {
                nextGeneration.get(row).add((byte) 0);
            }
        }

    }






}
