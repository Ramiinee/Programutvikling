package Game;


import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Scale;

public class Mouse {
    private Canvas Canvas;
    private Scale newScale;
    private double zoom_fac = 1.05;

    public Mouse(Canvas canvas ) {
        this.Canvas = canvas;
    }
    public void scroll(){


        Canvas.setOnScroll(event -> {

            newScale = new Scale();

            newScale.setPivotX(event.getX());
            newScale.setPivotY(event.getY());



            if (event.getDeltaY() > 0){
                newScale.setX( Canvas.getScaleX() * zoom_fac );
                newScale.setY( Canvas.getScaleY() * zoom_fac );
            }else {
                newScale.setX( Canvas.getScaleX() / zoom_fac );
                newScale.setY( Canvas.getScaleY() / zoom_fac );
            }


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
