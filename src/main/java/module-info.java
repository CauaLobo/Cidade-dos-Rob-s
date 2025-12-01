module org.example.pbl3java {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    // Exportações para JavaFX
    opens org.example.pbl3java to javafx.fxml;
    exports org.example.pbl3java;
    
    // Abre os pacotes para o Jackson poder usar reflexão na serialização/deserialização JSON
    opens model to com.fasterxml.jackson.databind;
    opens controller to com.fasterxml.jackson.databind;
    opens persistencia to com.fasterxml.jackson.databind;
}