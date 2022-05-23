package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class Question {

    String questionSubject;
    private String questionText;
    private Alternative[] alternatives;

    public Question(String questionSubject, String questionText, Alternative[] alternatives) throws IllegalArgumentException{
        boolean isThereACorrectAnswer = false;
        if (alternatives == null) {
            throw new IllegalArgumentException("Frågan måste ha svarsalternativ!");
        }
        for (Alternative a : alternatives)
            if(a.isCorrect) {
                isThereACorrectAnswer = true;
                break;
            }
        if (isThereACorrectAnswer) {
            this.questionSubject = questionSubject;
            this.questionText = questionText;
            this.alternatives = alternatives;
        } else {
            throw new IllegalArgumentException("En fråga måste ha något korrekt svarsalternativ.");
        }
    }

    public String getQuestionSubject() {
        return questionSubject;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Alternative[] getShuffledAlternatives() {
        Alternative[] shuffledAlternatives = alternatives;
        Collections.shuffle(Arrays.asList(shuffledAlternatives));
        return shuffledAlternatives;
    }

    public Alternative[] getCorrectAlternatives(boolean getCorrectAnswers) {
        ArrayList<Alternative> returnAlternatives = new ArrayList<>();
        for (Alternative a : alternatives) {
            if (getCorrectAnswers) {
                if (a.isCorrect) {
                    returnAlternatives.add(a);
                }
            } else {
                if (!a.isCorrect) {
                    returnAlternatives.add(a);
                }
            }
        }
        return returnAlternatives.toArray(new Alternative[returnAlternatives.size()]);
    }
}
