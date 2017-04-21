package Game.Controller;

import javafx.scene.control.*;

/**
 *
 * @author Joachim-Privat
 */
public class Waring {

    /**
     *
     * @param title
     * @param head
     * @param content
     */
    public void warning(String title, String head, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
