package Game;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.applet.Applet;
import java.util.Random;


public class Controller extends Applet implements Runnable{
    @FXML
    public Button Start;

    public Button Stop;
    @FXML
    public Label label;

    private Thread t, t1;
    private int runCount = 0;
    private int stopCount = 0;

    @FXML
    private Canvas Canvas;

    private int maxHeith = 50;
    private int maxWidth = 40;

    private byte[][] board = new byte[maxHeith][maxWidth];

    private boolean running = true;


    private GraphicsContext gc;

    @FXML
    private void test(){
        if (stopCount == 0){
            draw_Array();
            makeBoard(0);

        }
        t = new Thread (this);
        t.start();

        stopCount = 0;
        Start.setDisable(true);
        Stop.setDisable(false);
    }

    @FXML
    public void stop(){
        t.stop();
        Start.setDisable(false);
        if (stopCount == 1){
            remove_Array();
            stopCount = 0;
            Stop.setDisable(true);
        }
        stopCount++;

    }



    @FXML
    public void run()  {

        t1 = Thread.currentThread();

        while(t1 == t){
            draw_Array();

            try{ t1.sleep(500);
            }
            catch(InterruptedException e) {}
            remove_Array();
            makeBoard(1);



        }

    }



    private void makeBoard( int p){
        Random r = new Random();

        for (int i = 0; i < board.length ; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                int e = r.nextInt(50);
                if (p == 1){
                    if (e < 25) board[i][j] = 1;
                    else board[i][j] = 0;
                }
                else {
                    board[i][j] = 0;
                }

            }
        }


    }


    private void draw_Array() {
        try {

            gc = Canvas.getGraphicsContext2D();

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    if (board[i][j] == 0) {

                        draw(i, j, Color.BLACK);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    public void remove_Array() {
        try {

            Canvas.getGraphicsContext2D();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {

                        draw(i, j, Color.WHITE);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void draw(int x, int y, Color c) {
        gc.setFill(c);
        gc.fillRect(x * 10, y * 10, 10, 10);
    }
}
