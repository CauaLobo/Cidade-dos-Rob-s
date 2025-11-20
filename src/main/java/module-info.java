module org.example.pbl3java {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens org.example.pbl3java to javafx.fxml;
    exports org.example.pbl3java;
}