/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Controller;

import Game.Model.Boards.StaticBoard;
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
    };
        
StaticBoard gol = new StaticBoard();
gol.setBoard(board);
gol.(); 
org.junit.Assert.assertEquals("00010001000100000", gol.toString());
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
   

   
}
