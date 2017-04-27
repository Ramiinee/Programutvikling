package Game.Controller;



import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

/**
 *
 * @author Joachim-Privat
 */
public class Mouse {
    private Canvas Canvas;



    private Scale newScale;
    private double zoom_fac = 1.05;
    private double zoomValue = 0;

    /**
     *
     * @param canvas
     */
    public Mouse(Canvas canvas) {
        this.Canvas = canvas;


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

            if (zoomValue < 30 && zoomValue > -12){
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
                if (zoomValue >= 30&& event.getDeltaY() < 0) {
                    zoomValue--;
                    newScale.setX(Canvas.getScaleX() / zoom_fac);
                    newScale.setY(Canvas.getScaleY() / zoom_fac);
                } else if (zoomValue <= -12 && event.getDeltaY() > 0) {
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
