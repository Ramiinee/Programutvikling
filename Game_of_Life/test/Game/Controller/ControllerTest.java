/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class ControllerTest {
    public ColorPicker colorPicker;
    public BorderPane BoarderPane;
    public Slider timer;
    public Canvas Canvas;
    public ScrollPane scrollpane;
    public ComboBox<String> RuleDropDown;
    public Label BoardLabel;
    
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

    /**
     * Test of startStop method, of class Controller.
     */
    @Test
    public void testStartStop() {
        System.out.println("startStop");
        Controller instance = new Controller();
        instance.startStop();
        // TODO review the generated test code and remove the default call to fail.
       
    }

    @Test
    public void nextGenerationtest(){
        byte[][] board = {
            {0,0,1,0},
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}
    }    ; 
    }
    /**
     * Test of Clear method, of class Controller.
     */
    @Test
    public void testClear() {
        System.out.println("Clear");
        Controller instance = new Controller();
        instance.Clear();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of newBoard method, of class Controller.
     */
    @Test
    public void testNewBoard() {
        System.out.println("newBoard");
        Controller instance = new Controller();
        instance.newBoard();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadBoard method, of class Controller.
     */
    @Test
    public void testLoadBoard() {
        System.out.println("loadBoard");
        Controller instance = new Controller();
        instance.loadBoard();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Controller.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        Controller instance = new Controller();
        instance.reset();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class Controller.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        URL location = null;
        ResourceBundle resources = null;
        Controller instance = new Controller();
        instance.initialize(location, resources);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAwkColor method, of class Controller.
     */
    @Test
    public void testGetAwkColor() {
        System.out.println("getAwkColor");
        Color fx = null;
        Controller instance = new Controller();
        java.awt.Color expResult = null;
        java.awt.Color result = instance.getAwkColor(fx);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveBoard method, of class Controller.
     */
    @Test
    public void testSaveBoard() {
        System.out.println("saveBoard");
        Controller instance = new Controller();
        instance.saveBoard();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
