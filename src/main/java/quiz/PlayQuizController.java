package quiz;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Alternative;
import model.SingleChoiceQuestion;
import model.Question;
import java.util.ArrayList;

public class PlayQuizController {

    @FXML private TextArea questionTextArea;
    @FXML private Label titleLabel;
    @FXML private BorderPane alternativesBorderPane;
    @FXML private CheckMenuItem showCorrectAlternativesCheatCheckMenuItem;

    private Alternative[] currentAlternatives;
    private Question currentQuestion;
    private ArrayList<ButtonBase> alternativeBtnList;
    private static boolean wasCheatEnabled;

    @FXML
    void answerQuestion() {
        if (alternativeBtnList != null && !alternativeBtnList.isEmpty()) {
            ArrayList<Alternative> chosenAlternatives = new ArrayList<>();
            for (ButtonBase bB : alternativeBtnList) {
                for (Alternative a :currentAlternatives) {
                    boolean isAlternativeSelected = false;
                    if (bB instanceof CheckBox) {
                        if (((CheckBox) bB).isSelected()) {
                            isAlternativeSelected = true;
                        }
                    } else if (bB instanceof RadioButton) {
                        if (((RadioButton) bB).isSelected()) {
                            isAlternativeSelected = true;
                        }
                    }
                    if (isAlternativeSelected && bB.getText().equals(a.getAlternativeText())) {
                        chosenAlternatives.add(a);
                    }
                }
            }
            String gradedQuestion;
            if (QuizLogic.getQuizLogic().isQuestionCorrect(currentQuestion, chosenAlternatives.toArray(new Alternative[0]))) {
                gradedQuestion = "Du svarade rätt!";
            } else {
                gradedQuestion = "Du svarade fel! " + (currentQuestion.getCorrectAlternatives(true).length > 1 ? "De rätta svaren är:" : "Det rätta svaret är:");
                for (Alternative a : currentAlternatives) {
                    if (a.getIsCorrect())
                    gradedQuestion += "\n" + a.getAlternativeText();
                }
            }
            ShowAlert.showAlert(gradedQuestion);
            ViewController.showView(View.PLAY_QUIZ);
        } else {
            ShowAlert.showErrorAlert(new Exception("Alternativlistan är null eller saknar innehåll!"));
        }
    }

    @FXML void exitQuiz() { QuizLogic.getQuizLogic().exitQuiz();}
    @FXML void resetQuiz() {QuizLogic.getQuizLogic().resetQuiz();}

    @FXML
    void enableOrDisableAlternativesCheat() {
        if (showCorrectAlternativesCheatCheckMenuItem.isSelected()) {
            if (alternativeBtnList != null && !alternativeBtnList.isEmpty()) {
                for (ButtonBase bB : alternativeBtnList) {
                    for (Alternative a : currentAlternatives) {
                        if (a.getIsCorrect() && a.getAlternativeText().equals(bB.getText())) {
                            bB.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
                            wasCheatEnabled = true;
                        }
                    }
                }
            } else {
                ShowAlert.showErrorAlert(new Exception("Alternativlistan är null eller saknar innehåll!"));
            }
        } else {
            if (alternativeBtnList != null) {
                for (ButtonBase bB : alternativeBtnList) {
                    bB.setBackground(Background.EMPTY);
                    wasCheatEnabled = false;
                }
            } else {
                ShowAlert.showErrorAlert(new Exception("Alternativlistan är null eller saknar innehåll!"));
            }
        }
    }

    @FXML
    void showInfoAbout() {ShowAlert.showAlert("Här borde det stå något informativt men det är jobbigt att komma på vad.");}

    public void initialize() {
        currentQuestion = QuizLogic.getQuizLogic().getAQuestion();
        if (currentQuestion == null) {
            ShowAlert.showAlert("Grattis, du har klarat alla frågor!");
            resetQuiz();
        } else {
            titleLabel.setText(currentQuestion.getQuestionSubject());
            questionTextArea.setText(currentQuestion.getQuestionText());
            currentAlternatives = currentQuestion.getShuffledAlternatives();
            alternativesGridPaneSetup();
            setupCheatSettings();
        }
    }

    private void alternativesGridPaneSetup() {
        VBox alternativesVBox = new VBox();
        alternativesVBox.setPadding(new Insets(30));
        alternativesVBox.setSpacing(10);
        alternativeBtnList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();

        for (Alternative a : currentAlternatives) {
            if (currentQuestion instanceof SingleChoiceQuestion) {
                RadioButton rB = new RadioButton(a.getAlternativeText());
                rB.setToggleGroup(toggleGroup);
                alternativeBtnList.add(rB);
                alternativesVBox.getChildren().add(rB);
            } else {
                CheckBox cB = new CheckBox(a.getAlternativeText());
                alternativeBtnList.add(cB);
                alternativesVBox.getChildren().add(cB);
            }
        }
        alternativesBorderPane.setCenter(alternativesVBox);
    }

    private void setupCheatSettings() {
        if (wasCheatEnabled) {
            showCorrectAlternativesCheatCheckMenuItem.setSelected(true);
        }
        enableOrDisableAlternativesCheat();
    }
}
