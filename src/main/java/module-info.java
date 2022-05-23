module Quiz {
    requires javafx.controls;
    requires javafx.fxml;


    opens quiz to javafx.fxml;
    exports quiz;
}