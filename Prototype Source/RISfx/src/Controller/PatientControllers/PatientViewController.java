package Controller.PatientControllers;

import Controller.AppointmentControllers.*;
import Controller.Controller;
import Controller.Main;
import Model.Patient;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientViewController implements Initializable {

    @FXML
    TextField
            fNameField,    lNameField,      pNumberField,
            addressField,  dobField,        sNumberField,
            emailField,    InsuranceField,  balanceField;
    @FXML
    Button EditPatientInfoButton;

    private boolean EditPatientLock = false;

    public static void setView()throws Exception{
        Main.setCenterPane("PatientViews/PatientView.fxml");
    }

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }
    public void initialize(URL url, ResourceBundle arg1) {
        fNameField.setText(Main.getPatientFocus().getFirstname());
        lNameField.setText(Main.getPatientFocus().getLastname());
        pNumberField.setText(String.valueOf(Main.getPatientFocus().getPhoneNumber()));
        addressField.setText(Main.getPatientFocus().getAddress());
        dobField.setText(Main.getPatientFocus().getDob());
        emailField.setText(Main.getPatientFocus().getEmail());
        InsuranceField.setText(String.valueOf(Main.getPatientFocus().getInsuranceNumber()));
    }

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
            //TODO: Add code for submitting changed info
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
        AppointmentViewController.setView();
    }

    public void setAddAppointment(ActionEvent actionEvent) throws Exception {
        AddAppointmentController.setView();
    }
}
