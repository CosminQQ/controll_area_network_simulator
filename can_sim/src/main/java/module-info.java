module proj.can_sim {
    requires javafx.controls;
    requires javafx.fxml;


    opens proj.can_sim to javafx.fxml;
    exports proj.can_sim;
}