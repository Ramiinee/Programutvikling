package Game;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;



public class Controller {

    public Button Start;
    public Button Stop;

    @FXML
    private Canvas graphics;



    private byte[][] board = {
            {1, 0, 0, 1},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {1, 0, 0, 1}
    };


    

    @FXML
    private void draw_Array() {
        try {

            GraphicsContext gc = graphics.getGraphicsContext2D();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 1) {

                        draw(i, j, Color.BLACK, gc);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

    @FXML
    private void remove_Array() {
        try {
            GraphicsContext gc = graphics.getGraphicsContext2D();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 1) {

                        draw(i, j, Color.WHITE, gc);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        
    }

    private void draw(int x, int y, Color c, GraphicsContext gc) {
        gc.setFill(c);
        gc.fillRect(x * 10, y * 10, 10, 10);
    }
}
