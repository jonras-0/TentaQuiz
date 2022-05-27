package quiz;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Alternative;
import model.SingleChoiceQuestion;
import model.Question;

import java.io.IOException;
import java.util.ArrayList;

public class PlayQuizController {

    @FXML private TextArea questionTextArea;
    @FXML private Label titleLabel;
    @FXML private BorderPane alternativesBorderPane;
    @FXML private Label remainingQuestionsLabel;
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
                gradedQuestion = "Du svarade rätt" + (showCorrectAlternativesCheatCheckMenuItem.isSelected() ? " med hjälp av fusk. Bra jobbat!" : "!");
            } else {
                gradedQuestion = "Du svarade fel! " + (currentQuestion.getCorrectAlternatives(true).length > 1 ? "De rätta svaren är:" : "Det rätta svaret är:");
                for (Alternative a : currentAlternatives) {
                    if (a.getIsCorrect())
                    gradedQuestion += "\n" + a.getAlternativeText();
                }
            }
            ShowAlert.showAlert(gradedQuestion);
            if (QuizLogic.getQuizLogic().isGameReady()) {
                ViewController.showView(View.PLAY_QUIZ_VIEW);
            } else {
                ViewController.showView(View.RESULT_VIEW);
            }

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
        if (currentQuestion != null) {
            titleLabel.setText(currentQuestion.getQuestionSubject());
            questionTextArea.setText(currentQuestion.getQuestionText());
            currentAlternatives = currentQuestion.getShuffledAlternatives();
            alternativesGridPaneSetup();
            setupCheatSettings();
            int remainingQestions = QuizLogic.getQuizLogic().getNoOfRemainingQuestions();
            remainingQuestionsLabel.setText(remainingQestions + " " + (remainingQestions == 1 ? "fråga" : "frågor")
            + " kvar");
        }
    }

    private void alternativesGridPaneSetup() {
        VBox alternativesVBox = new VBox();
        alternativesVBox.setPadding(new Insets(30));
        alternativesVBox.setSpacing(10);
        alternativeBtnList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();

//        for (int i = 1 ; i <= currentAlternatives.length; i++) {
//            if (currentQuestion instanceof SingleChoiceQuestion) {
//                RadioButton rB = new RadioButton(i + ". " + currentAlternatives[i].getAlternativeText());
//                rB.setWrapText(true);
//                rB.setToggleGroup(toggleGroup);
//                alternativeBtnList.add(rB);
//                alternativesVBox.getChildren().add(rB);
//            } else {
//                CheckBox cB = new CheckBox(i + ". " + currentAlternatives[i].getAlternativeText());
//                alternativeBtnList.add(cB);
//                cB.setWrapText(true);
//                alternativesVBox.getChildren().add(cB);
//            }
//        }
        int counter = 1;
        for (Alternative a : currentAlternatives) {
            if (currentQuestion instanceof SingleChoiceQuestion) {
                RadioButton rB = new RadioButton(counter + ". " + a.getAlternativeText());
                rB.setWrapText(true);
                rB.setToggleGroup(toggleGroup);
                alternativeBtnList.add(rB);
                alternativesVBox.getChildren().add(rB);
            } else {
                CheckBox cB = new CheckBox(counter + ". " + a.getAlternativeText());
                alternativeBtnList.add(cB);
                cB.setWrapText(true);
                alternativesVBox.getChildren().add(cB);
            }
            counter++;
        }
        alternativesBorderPane.setCenter(alternativesVBox);
    }

    private void setupCheatSettings() {
        if (wasCheatEnabled) {
            showCorrectAlternativesCheatCheckMenuItem.setSelected(true);
        }
        enableOrDisableAlternativesCheat();
    }

    @FXML
    private void btnPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if (keyCode.equals(KeyCode.ENTER))
            answerQuestion();
        else if (keyCode.isDigitKey()){
            KeyCode[] digitKeyCodes = {KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3,
                    KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6,
                    KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9};
            for (int i = 0; i < digitKeyCodes.length; i++) {
                if (keyCode.equals(digitKeyCodes[i])) {
                    checkOrUncheckAlternative(i);
                    break;
                }
            }
        }
    }
    private void checkOrUncheckAlternative(int btnIndex) {
        if (alternativeBtnList.size() > btnIndex) {
            ButtonBase button = alternativeBtnList.get(btnIndex);
            if (button instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) button;
                if (!radioButton.isSelected())
                    radioButton.setSelected(true);
            } else if (button instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) button;
                if (checkBox.isSelected())
                    checkBox.setSelected(false);
                else
                    checkBox.setSelected(true);
            }
        }

    }
}
