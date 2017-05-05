/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Model.Boards;

import java.util.concurrent.CyclicBarrier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ida
 */
public class StaticBoardTest {
    
    public StaticBoardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getColumn method, of class StaticBoard.
     */
    @Test
    public void testGetColumn() {
        System.out.println("getColumn");
        StaticBoard instance = new StaticBoard();
        int expResult = 0;
        int result = instance.getColumn();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setColumn method, of class StaticBoard.
     */
    @Test
    public void testSetColumn() {
        System.out.println("setColumn");
        int column = 0;
        StaticBoard instance = new StaticBoard();
        instance.setColumn(column);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRow method, of class StaticBoard.
     */
    @Test
    public void testGetRow() {
        System.out.println("getRow");
        StaticBoard instance = new StaticBoard();
        int expResult = 0;
        int result = instance.getRow();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRow method, of class StaticBoard.
     */
    @Test
    public void testSetRow() {
        System.out.println("setRow");
        int row = 0;
        StaticBoard instance = new StaticBoard();
        instance.setRow(row);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of makeNextGenArray method, of class StaticBoard.
     */
    @Test
    public void testMakeNextGenArray() {
        System.out.println("makeNextGenArray");
        StaticBoard instance = new StaticBoard();
        instance.makeNextGenArray();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nextGeneration method, of class StaticBoard.
     */
    @Test
    public void testNextGeneration() {
        System.out.println("nextGeneration");
        int start = 0;
        int stop = 0;
        CyclicBarrier cyclicBarrier = null;
        StaticBoard instance = new StaticBoard();
        instance.nextGeneration(start, stop, cyclicBarrier);
        org.junit.Assert.assertEquals("00010001000100000", gol.toString());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of slowlyCover method, of class StaticBoard.
     */
    @Test
    public void testSlowlyCover() {
        System.out.println("slowlyCover");
        int start = 0;
        int stop = 0;
        CyclicBarrier cyclicBarrier = null;
        StaticBoard instance = new StaticBoard();
        instance.slowlyCover(start, stop, cyclicBarrier);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noDeadCellsRule method, of class StaticBoard.
     */
    @Test
    public void testNoDeadCellsRule() {
        System.out.println("noDeadCellsRule");
        int start = 0;
        int stop = 0;
        CyclicBarrier cyclicBarrier = null;
        StaticBoard instance = new StaticBoard();
        instance.noDeadCellsRule(start, stop, cyclicBarrier);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countNeighbor method, of class StaticBoard.
     */
    @Test
    public void testCountNeighbor() {
        System.out.println("countNeighbor");
        int row = 0;
        int col = 0;
        StaticBoard instance = new StaticBoard();
        int expResult = 0;
        int result = instance.countNeighbor(row, col);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class StaticBoard.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        StaticBoard instance = new StaticBoard();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBoard method, of class StaticBoard.
     */
    @Test
    public void testSetBoard_byteArrArr() {
        System.out.println("setBoard");
        byte[][] board = null;
        StaticBoard instance = new StaticBoard();
        byte[][] expResult = null;
        byte[][] result = instance.setBoard(board);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCellAliveState method, of class StaticBoard.
     */
    @Test
    public void testGetCellAliveState() {
        System.out.println("getCellAliveState");
        int col = 0;
        int row = 0;
        StaticBoard instance = new StaticBoard();
        byte expResult = 0;
        byte result = instance.getCellAliveState(col, row);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCellAliveState method, of class StaticBoard.
     */
    @Test
    public void testSetCellAliveState() {
        System.out.println("setCellAliveState");
        int row = 0;
        int column = 0;
        byte aliveState = 0;
        StaticBoard instance = new StaticBoard();
        instance.setCellAliveState(row, column, aliveState);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of makeBoard method, of class StaticBoard.
     */
    @Test
    public void testMakeBoard() {
        System.out.println("makeBoard");
        int row = 0;
        int col = 0;
        StaticBoard instance = new StaticBoard();
        instance.makeBoard(row, col);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBoard method, of class StaticBoard.
     */
    @Test
    public void testSetBoard_0args() {
        System.out.println("setBoard");
        StaticBoard instance = new StaticBoard();
        instance.setBoard();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
