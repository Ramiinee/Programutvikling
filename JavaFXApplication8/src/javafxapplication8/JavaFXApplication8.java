/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication8;

import java.awt.Color;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ida
 */
public class JavaFXApplication8 extends Application {
    
    public static void main() throws Exception {
		
		// data related to the GIF image file
		String path = "testgif.gif";
		int width = 100;
		int height = 100;
		int timePerMilliSecond = 1000; // 1 second
		
		// create the GIFWriter object
		lieng.GIFWriter gwriter = new lieng.GIFWriter(width,height,path,timePerMilliSecond);
		
		// fill the upper half of the image with blue
		gwriter.fillRect(0, (width/2)-1, 0, height/2, Color.BLUE);
		
                
                gwriter.insertAndProceed();
                
                gwriter.fillRect((width/2)-1, (width/2)-1, 0, height/2, Color.BLUE);
		// insert the painted image to the animation sequence 
		// and proceed to the next image
		gwriter.insertAndProceed();
		
		// fill the lower half of the image with blue
		gwriter.fillRect(0, width-1, height/2, height-1, Color.BLUE);
		
		// insert the painted image into the animation sequence
		gwriter.insertCurrentImage();
		
		// close the GIF stream.
		gwriter.close();
		
		System.out.println("done!");
		
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
        main(); //To change body of generated methods, choose Tools | Templates.
    }
}


