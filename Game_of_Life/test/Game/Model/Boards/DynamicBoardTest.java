
package Game.Model.Boards;

import java.util.concurrent.CyclicBarrier;
import org.junit.Test;


public class DynamicBoardTest {

    
    
    @Test
    public void testNextGeneration() {
        System.out.println("nextGeneration");
         byte[][] board = {
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,0,1,1,0,0},
            {0,0,1,1,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            
    };
       
        DynamicBoard instance = new DynamicBoard();
        instance.makeBoard(board);
        int start = 0;
        int stop = instance.getColumn();
        instance.makeNextGenArray();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        instance.nextGeneration(start, stop, cyclicBarrier);
        instance.setBoard();
        org.junit.Assert.assertEquals("000000000000001100001100000000000000", instance.toString());
 
    }

    @Test
    public void testnoDeadCellsRule() {
        System.out.println("nextGeneration");
         byte[][] board = {
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,0,1,1,0,0},
            {0,0,1,1,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            
    };
       
        DynamicBoard instance = new DynamicBoard();
        instance.makeBoard(board);
        int start = 0;
        int stop = instance.getColumn();
        instance.makeNextGenArray();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        instance.noDeadCellsRule(start, stop, cyclicBarrier);
        instance.setBoard();
        org.junit.Assert.assertEquals("000000000000001100001100000000000000", instance.toString());
 
    }

    @Test
    public void testslowlyCover() {
        System.out.println("nextGeneration");
         byte[][] board = {
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,0,1,1,0,0},
            {0,0,1,1,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            
    };
       
        DynamicBoard instance = new DynamicBoard();
        instance.makeBoard(board);
        int start = 0;
        int stop = instance.getColumn();
        instance.makeNextGenArray();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        instance.slowlyCover(start, stop, cyclicBarrier);
        instance.setBoard();
        org.junit.Assert.assertEquals("000000000000001100001100000000000000", instance.toString());
 
    }
    @Test
    public void testcountNeighbor(){
         byte[][] board = {
            {0,0,1,0},              
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}
    };
       
        DynamicBoard instance = new DynamicBoard();
        instance.makeBoard(board);      
        org.junit.Assert.assertEquals(2,instance.countNeighbor(1, 2) );
 
    }

    
   

}
