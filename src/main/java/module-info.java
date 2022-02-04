/**
 *
 */
module com.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires java.sql;
    requires com.jfoenix;



    opens com.example.passwordmanager to javafx.fxml;
    exports com.example.passwordmanager;

    opens com.example.passwordmanager.DB to javafx.base;
    exports com.example.passwordmanager.DB;
    exports com.example.passwordmanager.LoginScreen;
    opens com.example.passwordmanager.LoginScreen to javafx.fxml;
    exports com.example.passwordmanager.LoginScreen.LoginScreenExceptions;
    opens com.example.passwordmanager.LoginScreen.LoginScreenExceptions to javafx.fxml;

}
