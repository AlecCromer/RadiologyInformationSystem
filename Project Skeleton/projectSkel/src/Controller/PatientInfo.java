package Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PatientInfo {
    @FXML
    private Label lblText;

    public void handleButton() {
        lblText.setText("");
    }
}
