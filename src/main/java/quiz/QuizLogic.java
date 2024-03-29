package quiz;

import model.Alternative;
import model.MultipleChoiceQuestion;
import model.SingleChoiceQuestion;
import model.Question;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class QuizLogic {
    private static QuizLogic quizLogic = new QuizLogic();
    private ArrayList<Question> questionList;
    private int rightAnswerCounter;
    private int wrongAnswerCounter;

    private QuizLogic() {
        rightAnswerCounter = 0;
        wrongAnswerCounter = 0;
    }

    public int getRightAnswerCounter() {return rightAnswerCounter;}
    public int getWrongAnswerCounter() {return wrongAnswerCounter;}
    public static QuizLogic getQuizLogic() {return quizLogic;}


    public boolean isGameReady() {
        if (questionList != null && !questionList.isEmpty()) {
            return true;
        }
        return false;
    }

    public Question getAQuestion() {
        if (questionList == null || questionList.isEmpty()) {
            return null;
        } else {
            shuffleQuestions();
            return questionList.get(0);
        }
    }

    public boolean isQuestionCorrect(Question question, Alternative[] chosenTrueAnswers) {
        ArrayList<Alternative> trueAnswers = new ArrayList<>(Arrays.asList(question.getCorrectAlternatives(true)));
        for (Alternative a : chosenTrueAnswers) {
            if (a.getIsCorrect()) {
                trueAnswers.remove(a);
            } else {
                wrongAnswerCounter++;
                return false;
            }
        }
        if (trueAnswers.isEmpty()) {
            removeCorrectlyAnsweredQuestion(question);
            rightAnswerCounter++;
            return true;
        }
        wrongAnswerCounter++;
        return false;
    }

    public void exitQuiz() {System.exit(0);}
    public void resetQuiz() {ViewController.showView(View.MENU_VIEW);}

    public void getQuestionsFromFile(File file) {
        try {
            questionList = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            String questionSubject = "";
            String questionText = null;
            ArrayList<Alternative> questionAlternatives = new ArrayList<>();
            boolean isSingleChoiceQuestion = false;
            int currentRow = 0;

            while (scanner.hasNextLine()) {
                String readText = scanner.nextLine();
                currentRow++;
                if (readText.isEmpty() || readText.startsWith(" ")) {
                    if (questionText != null && !questionText.isEmpty()) {
                        createQuestionAndAddToList(questionSubject, questionText, questionAlternatives.toArray(new Alternative[0]), isSingleChoiceQuestion);
                        questionSubject = "";
                        questionText = null;
                        questionAlternatives.clear();
                        isSingleChoiceQuestion = false;
                    }
                } else {
                    if (!readText.startsWith("//")) {
                        String[] readTextSplit = readText.split(" ", 2);
                        switch (readTextSplit[0].toLowerCase()) {
                            case "singlechoicequestion", "sq" -> isSingleChoiceQuestion = true;
                            case "multiplechoicequestion", "mq" -> isSingleChoiceQuestion = false;
                            case "subject:", "s" -> questionSubject = readTextSplit[1];
                            case "question:", "q" -> questionText = readTextSplit[1];
                            case "true:", "t" -> questionAlternatives.add(new Alternative(readTextSplit[1], true));
                            case "false:", "f" -> questionAlternatives.add(new Alternative(readTextSplit[1], false));
                            default -> throw new IllegalArgumentException(getDetailsFromQuestionCreationError(questionSubject,
                            questionText, questionAlternatives.toArray(new Alternative[0]), isSingleChoiceQuestion, currentRow));
                        }
                    }
                }
            }
            createQuestionAndAddToList(questionSubject, questionText, questionAlternatives.toArray(new Alternative[0]), isSingleChoiceQuestion);
        } catch (Exception e) {
            ShowAlert.showErrorAlert(e);
        }
    }

    public int getNoOfRemainingQuestions() {
        if (questionList != null) {
            return questionList.size();
        }
        else {
            return 0;
        }
    }

    private String getDetailsFromQuestionCreationError(String questionSubject,String questionText,
                   Alternative[] questionAlternatives, boolean isSingleChoiceQuestion, int row) {
        String errorMessage = "Tilldelade frågevariabler vid krasch på rad " + row + ":"
                + "\nFrågetypen är " + (isSingleChoiceQuestion ? "Envalsfråga" : "Flervalsfråga")
                + "\nFrågans ämne är: " + questionSubject
                + "\nFrågan är: " + questionText
                + "\nAlternativen är:";
        for (Alternative a : questionAlternatives) {
            errorMessage += "\n" + a.getAlternativeText();
        }
        return errorMessage;
    }

    private void createQuestionAndAddToList(String questionSubject, String questionText, Alternative[] questionAlternatives, boolean isSingleChoiceQuestion) {
        Question question;
        if (isSingleChoiceQuestion) {
            question = new SingleChoiceQuestion(questionSubject, questionText, questionAlternatives);
        }
        else {
            question = new MultipleChoiceQuestion(questionSubject, questionText, questionAlternatives);
        }
        if (question != null) {
            questionList.add(question);
        }
    }

    private void removeCorrectlyAnsweredQuestion(Question q) {
        if (questionList.contains(q)) {
            questionList.remove(q);
        } else {
            throw new RuntimeException("Frågan finns inte i frågelistan!");
        }
    }

    private void shuffleQuestions() {Collections.shuffle(questionList);}
}
