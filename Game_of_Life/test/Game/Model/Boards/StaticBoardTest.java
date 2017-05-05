
package Game.Model.Boards;

import java.util.concurrent.CyclicBarrier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class StaticBoardTest {
    
    
    @Test
    public void testNextGeneration() {
        System.out.println("nextGeneration");
         byte[][] board = {
            {0,0,1,0},              
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}
    };
       
        StaticBoard instance = new StaticBoard();
        instance.makeBoard(board);
        int start = 0;
        int stop = instance.getColumn();
        instance.makeNextGenArray();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        instance.nextGeneration(start, stop, cyclicBarrier);
        instance.setBoard();
        org.junit.Assert.assertEquals("0000011100000000", instance.toString());
 
    }

    @Test
    public void testnoDeadCellsRule() {
        System.out.println("nextGeneration");
         byte[][] board = {
            {0,0,1,0},              
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}
    };
       
        StaticBoard instance = new StaticBoard();
        instance.makeBoard(board);
        int start = 0;
        int stop = instance.getColumn();
        instance.makeNextGenArray();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        instance.noDeadCellsRule(start, stop, cyclicBarrier);
        instance.setBoard();
        org.junit.Assert.assertEquals("0010011100100000", instance.toString());
 
    }

    @Test
    public void testslowlyCover() {
        System.out.println("nextGeneration");
         byte[][] board = {
            {0,0,1,0},              
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}
    };
       
        StaticBoard instance = new StaticBoard();
        instance.makeBoard(board);
        int start = 0;
        int stop = instance.getColumn();
        instance.makeNextGenArray();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        instance.slowlyCover(start, stop, cyclicBarrier);
        instance.setBoard();
        org.junit.Assert.assertEquals("0010001000100000", instance.toString());
 
    }
    @Test
    public void testcountNeighbor(){
         byte[][] board = {
            {0,0,1,0},              
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}
    };
       
        StaticBoard instance = new StaticBoard();
        instance.makeBoard(board);      
        org.junit.Assert.assertEquals(2,instance.countNeighbor(1, 2) );
 
    }
}
