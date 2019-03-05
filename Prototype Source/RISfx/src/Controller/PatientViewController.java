package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PatientViewController {

    @FXML
    TextField
            fNameField,    lNameField,      pNumberField,
            addressField,  dobField,        sNumberField,
            emailField,    InsuranceField,  balanceField;
    @FXML
    Button EditPatientInfoButton;

    //Allows the editing of the patient's information
    public void editPatientInfo(ActionEvent actionEvent) throws Exception {
        if (!EditPatientLock) {
            EditPatientLock = true;
            fNameField.setDisable(false);
            lNameField.setDisable(false);
            pNumberField.setDisable(false);
            addressField.setDisable(false);
            dobField.setDisable(false);
            sNumberField.setDisable(false);
            InsuranceField.setDisable(false);
            balanceField.setDisable(false);
            emailField.setDisable(false);
            EditPatientInfoButton.setText("Submit");
        }
        else{
            EditPatientLock = false;
            fNameField.setDisable(true);
            lNameField.setDisable(true);
            pNumberField.setDisable(true);
            addressField.setDisable(true);
            dobField.setDisable(true);
            sNumberField.setDisable(true);
            emailField.setDisable(true);
            InsuranceField.setDisable(true);
            balanceField.setDisable(true);
            EditPatientInfoButton.setText("Edit Patient Info.");
        }
    }

    public void setAppointmentView(ActionEvent actionEvent) throws Exception {
        AppointmentController.setAppointmentView();
    }

    public void setAddAppointment(ActionEvent actionEvent) throws Exception {
        AppointmentController.setAddAppointment();
    }

    private boolean EditPatientLock = false;

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }
}
