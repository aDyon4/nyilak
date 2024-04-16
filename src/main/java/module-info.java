module com.example.nyilak {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nyilak to javafx.fxml;
    exports com.example.nyilak;
}