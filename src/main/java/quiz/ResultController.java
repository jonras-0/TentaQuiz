package quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultController {

    @FXML private Label noOfRightsLabel;
    @FXML private Label noOfWrongsLabel;
    @FXML void exitQuiz() {QuizLogic.getQuizLogic().exitQuiz();}
    @FXML void restartQuiz() {QuizLogic.getQuizLogic().resetQuiz();}

    public void initialize() {
        noOfRightsLabel.setText(String.valueOf(QuizLogic.getQuizLogic().getRightAnswerCounter()));
        noOfWrongsLabel.setText(String.valueOf(QuizLogic.getQuizLogic().getWrongAnswerCounter()));
    }
}