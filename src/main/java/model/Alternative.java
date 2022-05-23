package model;

public class Alternative {
    private String alternativeText;
    boolean isCorrect;

    public Alternative(String alternativeText, boolean isCorrect) {
        this.alternativeText = alternativeText;
        this.isCorrect = isCorrect;
    }

    public boolean getIsCorrect() {return isCorrect;}


    public String getAlternativeText() {return alternativeText;}
}
