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
                return false;
            }
        }
        if (trueAnswers.isEmpty()) {
            removeCorrectlyAnsweredQuestion(question);
            return true;
        }
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

            while (scanner.hasNextLine()) {
                String readText = scanner.nextLine();
                if (readText.isEmpty()) {
                    if (questionText != null && !questionText.isEmpty()) {
                        createQuestionAndAddToList(questionSubject, questionText, questionAlternatives.toArray(new Alternative[0]), isSingleChoiceQuestion);
                        questionSubject = "";
                        questionText = null;
                        questionAlternatives.clear();
                        isSingleChoiceQuestion = false;
                    }
                } else {
                    String[] readTextSplit = readText.split(" ", 2);
                    if (!readTextSplit[0].equals("//")) {
                        switch (readTextSplit[0]) {
                            case "singleChoiceQuestion", "sq" -> isSingleChoiceQuestion = true;
                            case "multipleChoiceQuestion", "mq" -> isSingleChoiceQuestion = false;
                            case "subject:", "s" -> questionSubject = readTextSplit[1];
                            case "question:", "q" -> questionText = readTextSplit[1];
                            case "true:", "t" -> questionAlternatives.add(new Alternative(readTextSplit[1], true));
                            case "false:", "f" -> questionAlternatives.add(new Alternative(readTextSplit[1], false));
                            default -> throw new IllegalArgumentException("Filen följer inte förväntat format!");
                        }
                    }
                }
            }
            createQuestionAndAddToList(questionSubject, questionText, questionAlternatives.toArray(new Alternative[0]), isSingleChoiceQuestion);
        } catch (Exception e) {
            ShowAlert.showErrorAlert(e);
        }
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
