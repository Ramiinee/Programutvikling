package Game;



import Game.StaticBoard;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

/**
 *
 * @author Joachim-Privat
 */
public class Mouse {
    private Canvas Canvas;
    private StaticBoard staticBoard;
    private ScrollPane scrollpane;

    private Scale newScale;
    private double zoom_fac = 1.05;
    private double zoomValue = 0;

    /**
     *
     * @param canvas
     * @param staticBoard
     */
    public Mouse(Canvas canvas, StaticBoard staticBoard, ScrollPane scrollPane) {
        this.Canvas = canvas;
        this.staticBoard = staticBoard;
        this.scrollpane = scrollPane;

    }

    /**
     *
     * @return
     */
    public double getZoomValue() {
        return zoomValue;
    }

    /**
     *
     * @param zoomValue
     */
    public void setZoomValue(double zoomValue) {
        this.zoomValue = zoomValue;
    }

    /**
     * Dette er en test funksjon for zoom og click events.
     */
    public void scroll(){

        Canvas.setOnScroll((ScrollEvent event) -> {
            if (event.isControlDown()){

            }
            newScale = new Scale();
            newScale.setPivotX(event.getX());
            newScale.setPivotY(event.getY());

            if (zoomValue < 20 && zoomValue > -11){
                if (event.getDeltaY() > 0){
                    zoomValue++;
                    newScale.setX( Canvas.getScaleX() * zoom_fac );
                    newScale.setY( Canvas.getScaleY() * zoom_fac );
                /*
                 if (st >= 10){
                     slider.setValue(size*zoom_fac);
                     st = st + zoom_fac;
                     System.out.println("");
                     System.out.println(event.getDeltaY());
                     System.out.println(st);
                     System.out.println(zoom_fac);

                 }
                 */
                }else {
                    zoomValue--;
                    newScale.setX( Canvas.getScaleX() / zoom_fac );
                    newScale.setY( Canvas.getScaleY() / zoom_fac );
                    /*
                       if (st > 10){
                     slider.setValue(size/zoom_fac);
                     st = st  zoom_fac;
                     System.out.println("");
                     System.out.println(event.getDeltaY());
                     System.out.println(st);
                     System.out.println(zoom_fac);
                 }
                 */

                }
            }
            else {
                if (zoomValue >= 20 && event.getDeltaY() < 0) {
                    zoomValue--;
                    newScale.setX(Canvas.getScaleX() / zoom_fac);
                    newScale.setY(Canvas.getScaleY() / zoom_fac);
                } else if (zoomValue <= -11 && event.getDeltaY() > 0) {
                    zoomValue++;
                    newScale.setX(Canvas.getScaleX() * zoom_fac);
                    newScale.setY(Canvas.getScaleY() * zoom_fac);
                }
            }

            Canvas.getTransforms().add(newScale);
            event.consume();
        });
    }

    /**
     *
     * @return
     */
    public Scale getNewScale() {
        return newScale;
    }

    /**
     *
     * @return
     */
    public double getZoom_fac() {
        return zoom_fac;
    }

    /**
     *
     * @param zoom_fac
     */
    public void setZoom_fac(double zoom_fac) {
        this.zoom_fac = zoom_fac;
    }
}
