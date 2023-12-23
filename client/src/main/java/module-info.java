module com.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires com.google.gson;

    opens com.client to javafx.fxml;
    opens com.client.controllers to javafx.fxml;

    exports com.client;
}
