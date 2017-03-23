/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
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
public class ControllerTest {
    
    public ControllerTest() {
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

    /* @Test
    public void testTimerlistener() {
    System.out.println("timerlistener");
    Controller instance = new Controller();
    instance.timerlistener();
    // TODO review the generated test code and remove the default call to fail.
    fail("det ble en fail. ");
    }
    

    @Test
    public void testStartButton() {
        System.out.println("startButton");
        Controller instance = new Controller();
        instance.startButton();
        // TODO review the generated test code and remove the default call to fail.
        fail("vet egentlig ikke hva jeg skal skrive her enda, men fail");
    }
    

    @Test
    public void testStopButton() {
        System.out.println("stopButton");
        Controller instance = new Controller();
        instance.stopButton();
        // TODO review the generated test code and remove the default call to fail.
        fail("noe er galt");
    }

    @Test
    public void testLoad() {
        System.out.println("load");
        Controller instance = new Controller();
        instance.load();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testRandomBoard() {
        System.out.println("RandomBoard");
        Controller instance = new Controller();
        instance.RandomBoard();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testShowClearBoard() {
        System.out.println("showClearBoard");
        Controller instance = new Controller();
        instance.showClearBoard();
        // TODO review the generated test code and remove the default call to fail.
        fail("vet ikke om testen funker");
    }
    

    @Test
    public void testGetCanvas() {
        System.out.println("getCanvas");
        Controller instance = new Controller();
        Canvas expResult = null;
        Canvas result = instance.getCanvas();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("i think its a fail");
    }
    
    @Test
    public void testGetCanvas1() {
        System.out.println("getCanvas");
        Controller instance = new Controller();
        Canvas expResult = null;
        Canvas result = instance.getCanvas();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("what am i doing with my life");
    }
    

    @Test
    public void testResetSlider() {
        System.out.println("resetSlider");
        Controller instance = new Controller();
        instance.resetSlider();
        // TODO review the generated test code and remove the default call to fail.
        fail("ugh");
    }
    

    */
    @Test
    public void testnextgeneration(){
        byte[][] board = {
            {0,0,0,0},
            {0,1,1,0},
            {0,1,1,0},
            {0,0,0,0}   
        };
        Controller gol = new Controller();
        gol.setBoard(board);
        gol.nextGeneration();
        org.junit.Assert.assertEquals(gol.toString(), "0000011001100000");
    }
    
    /*@Test
    public void testInitialize() {
    System.out.println("initialize");
    URL location = null;
    ResourceBundle resources = null;
    Controller instance = new Controller();
    instance.initialize(location, resources);
    // TODO review the generated test code and remove the default call to fail.
    fail(" =^.^= fail");
    }
    
    Test of onScrollEventHandler method, of class Controller.
    
    @Test
    public void testOnScrollEventHandler() {
        System.out.println("onScrollEventHandler");
        ScrollEvent scrollEvent = null;
        Controller instance = new Controller();
        instance.onScrollEventHandler(scrollEvent);
        // TODO review the generated test code and remove the default call to fail.
        fail("ahhah fail");
    }
    */
}
