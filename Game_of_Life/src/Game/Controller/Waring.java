package Game.Controller;

import javafx.scene.control.*;


public class Waring {

    /**
     * Generates a poppup warning if the user do something its not supposed to do. 
     * @param title title of the warning
     * @param head header of the warning
     * @param content content in the warning
     */
    public void warning(String title, String head, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
