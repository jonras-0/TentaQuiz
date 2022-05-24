package quiz;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Paths;

public class MenuController {

    @FXML private TextField filePathTextField;

    @FXML
    void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Öppna fil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        fileChooser.setInitialDirectory(new File(Paths.get("./files").toAbsolutePath().normalize().toString()));
        File file = fileChooser.showOpenDialog(ViewController.getStage());
        if (file != null) {
            filePathTextField.setText(file.getAbsolutePath());
            QuizLogic.getQuizLogic().getQuestionsFromFile(file);
        }
    }

    @FXML
    void startQuiz() {
        if (QuizLogic.getQuizLogic().isGameReady()) {
            ViewController.showView(View.PLAY_QUIZ_VIEW);
        }
        else {
            ShowAlert.showInformationAlert("En fil behöver läsas in innan du kan starta quizen!");
        }
    }

}
