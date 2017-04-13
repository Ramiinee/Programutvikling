package Game;

import javafx.scene.control.*;

public class Waring {

    public void warning(String title, String head, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
