package quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewController extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        showView(View.MENU_VIEW);
    }

    public static void showView(View view) {
        FXMLLoader fxmlLoader = new FXMLLoader((ViewController.class.getResource(view.getFxmlFile())));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ShowAlert.showErrorAlert(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
    public static Stage getStage() {
        return stage;
    }
}