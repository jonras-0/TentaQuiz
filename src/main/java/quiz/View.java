package quiz;

public enum View {
    MENU_VIEW("Menu.fxml"),
    PLAY_QUIZ ("PlayQuiz.fxml");

    String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
