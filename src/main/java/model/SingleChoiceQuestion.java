package model;

/**
 * Specialisering för frågor där bara ett alternativ kan väljas som rätt svar.
 */
public class SingleChoiceQuestion extends Question {

    public SingleChoiceQuestion(String questionSubject, String questionText, Alternative[] alternatives) throws IllegalArgumentException {
        super(questionSubject, questionText, alternatives);
        int nrOfRightAnswers = 0;
        for (Alternative a : alternatives) {
            if (a.isCorrect)
                nrOfRightAnswers++;
        }
        if (nrOfRightAnswers < 1) {
            throw new IllegalArgumentException("En envalsfråga får inte ha flera korrekta svarsalternativ.");
        }
    }
}
