module com.example.hw6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hw6 to javafx.fxml;
    exports com.example.hw6;
}