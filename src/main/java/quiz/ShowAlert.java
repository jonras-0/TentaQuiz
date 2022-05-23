package quiz;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowAlert {

    public static void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Meddelande");
        alert.setContentText(text);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.initOwner(ViewController.getStage());
        alert.showAndWait();
    }
    public static void showInformationAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(text);
        alert.initOwner(ViewController.getStage());
        alert.showAndWait();
    }

    public static void showErrorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getMessage());
        alert.setContentText(e.toString());
        alert.initOwner(ViewController.getStage());
        alert.showAndWait();
    }
}
