/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
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


    //tester om next generation funker som den skal. 
    @Test
    public void testnextgeneration(){
        byte[][] board = {
            {0,0,1,0},
            {0,0,1,0},
            {0,0,1,0},
            {0,0,0,0}   
        };
        Controller gol = new Controller();
        gol.setBoard(board);
        gol.nextGeneration();
        org.junit.Assert.assertEquals(gol.toString(), "0000011100000000");
        gol.nextGeneration();
        org.junit.Assert.assertEquals(gol.toString(),"0010001000100000");

    }
   
}
