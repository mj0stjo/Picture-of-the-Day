module com.routine.pictureoftheday {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.routine.pictureoftheday to javafx.fxml;
    exports com.routine.pictureoftheday;
}