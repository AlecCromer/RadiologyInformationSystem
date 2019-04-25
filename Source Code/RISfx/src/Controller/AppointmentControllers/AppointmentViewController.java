package Controller.AppointmentControllers;

import Controller.Main;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentViewController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField     pNameField, appointmentIDField, appointmentDateField,
                        appointmentTimeField, signInField, signOutField;
    @FXML
    ComboBox<String>    checkInOutBox;
    @FXML Button        submitCheckInOut;
    private String comboSelection = "";


      ////////////////
     //Initializers//
    ////////////////

    /**
     * Sets AppointmentView.fxml to center pane
     * @throws Exception
     */
    public static void setView() throws Exception {
        Main.setCenterPane("AppointmentViews/AppointmentView.fxml");
    }

    @SuppressWarnings("Duplicates")
    /**
     * Automatically fills FXML fields with info from selected appointment object
     */
    public void initialize(URL url, ResourceBundle arg1) {
        pNameField.setText(Main.getAppointmentFocus().getPatientFullName());
        appointmentIDField.setText(String.valueOf(Main.getAppointmentFocus().getAppointmentId()));
        appointmentDateField.setText((new SimpleDateFormat("MM/dd/yyyy")).format(Main.getAppointmentFocus().getAppointmentDate()));
        appointmentTimeField.setText((new SimpleDateFormat("HH:mm")).format(Main.getAppointmentFocus().getAppointmentTime()));
        if (Main.getAppointmentFocus().getPatientSignIn() != null) {
            signInField.setText((new SimpleDateFormat("HH:mm")).format(Main.getAppointmentFocus().getPatientSignIn()));
        }
        if (Main.getAppointmentFocus().getPatientSignOut() != null) {
            signOutField.setText((new SimpleDateFormat("HH:mm")).format(Main.getAppointmentFocus().getPatientSignOut()));
        }

        try {
            comboBoxFill();
        }
        catch (Exception e){ e.printStackTrace(); }
    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////

    /**
     * Sets the view for appointment report form
     * @param actionEvent
     * @throws Exception
     */
    public void showAppointmentFiles(ActionEvent actionEvent) throws Exception {
        AppointmentFilesController.setView();
    }

    /**
     * Onclick method that reverts to previous page by calling Main.setBackPage()
     * @throws Exception
     */
    public void setBackPage() throws Exception {
        Main.setBackPage();
    }

    /**
     * Changes the patient's status to either signed in or signed out
     * @throws Exception
     */
    public void checkPatient() throws Exception{
        if(signInField.getText().length() > 0){
            Main.getAppointmentFocus().setPatientSignIn(Time.valueOf(LocalTime.parse(signInField.getText())));
            Appointment.updateSignInTime();
        }
        if(signOutField.getText().length() > 0){
            Main.getAppointmentFocus().setPatientSignOut(Time.valueOf(LocalTime.parse(signOutField.getText())));
            Appointment.updateSignOutTime();
        }
        AppointmentViewController.setView();
        Main.popBackNodeList();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private void comboBoxFill() throws Exception{
        ArrayList<String> checkInOut = new ArrayList<String>();
        if (Main.getAppointmentFocus().getPatientSignIn() == null) {
            checkInOut.add("Check In");
        }
        if (Main.getAppointmentFocus().getPatientSignOut() == null && Main.getAppointmentFocus().getPatientSignIn() != null) {
            checkInOut.add("Check Out");
        }
        //Disable if the patient has been checked in and out
        if (checkInOut.size() == 0){
            checkInOutBox.setDisable(true);
            submitCheckInOut.setDisable(true);
        }
        checkInOutBox.setItems(FXCollections.observableArrayList(checkInOut));
        checkInOutBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            try {
                comboSelection = checkInOutBox.getValue();
                switch (comboSelection){
                    case "Check In":
                        signInField.setText(LocalTime.now().toString());
                        break;
                    case "Check Out":
                        signOutField.setText(LocalTime.now().toString());
                        break;
                    default: break;
                }
            }catch (Exception e) { e.printStackTrace(); }
        });
    }
}
