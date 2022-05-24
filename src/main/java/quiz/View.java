package quiz;

public enum View {
    MENU_VIEW("Menu.fxml"), PLAY_QUIZ_VIEW("PlayQuiz.fxml"), RESULT_VIEW("Result.fxml");

    String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
