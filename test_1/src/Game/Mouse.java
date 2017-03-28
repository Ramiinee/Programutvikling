package Game;


import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

public class Mouse {
    private Canvas Canvas;
    private Slider slider;
    private Scale newScale;
    private double zoom_fac = 1.05;
    private double zoomValue = 0;

    public Mouse(Canvas canvas, Slider slider) {
        this.Canvas = canvas;
        this.slider = slider;
    }

    public double getZoomValue() {
        return zoomValue;
    }

    public void setZoomValue(double zoomValue) {
        this.zoomValue = zoomValue;
    }

    public void scroll(){

        Controller controller = new Controller();

        Canvas.setOnScroll((ScrollEvent event) -> {

            newScale = new Scale();

            newScale.setPivotX(event.getX());
            newScale.setPivotY(event.getY());



            if (zoomValue < 20 && zoomValue > -10){
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
                } else if (zoomValue <= -10 && event.getDeltaY() > 0) {
                    zoomValue++;
                    newScale.setX(Canvas.getScaleX() * zoom_fac);
                    newScale.setY(Canvas.getScaleY() * zoom_fac);
                }
            }


            controller.setSize(slider);
            Canvas.getTransforms().add(newScale);




            event.consume();
        });
    }

    public Scale getNewScale() {
        return newScale;
    }

    public double getZoom_fac() {
        return zoom_fac;
    }

    public void setZoom_fac(double zoom_fac) {
        this.zoom_fac = zoom_fac;
    }
}
