module com.example.snakur {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.snakur to javafx.fxml;
    exports com.example.snakur;
}
