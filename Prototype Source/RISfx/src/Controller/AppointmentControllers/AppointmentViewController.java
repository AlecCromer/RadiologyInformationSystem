package Controller.AppointmentControllers;

import Controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AppointmentViewController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML
    TextField pNameField, appointmentIDField, appointmentDateField, appointmentTimeField, signInField, signOutField;


      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception {
        Main.setCenterPane("AppointmentViews/AppointmentView.fxml");
    }

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
    }


      ////////////////////
     //Database Queries//
    ////////////////////


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    public void showAppointmentFiles(ActionEvent actionEvent) throws Exception {
        AppointmentFilesController.setView();
    }

    public void setBackPage() throws Exception {
        Main.setBackPage();
    }


      ///////////////////
     //Form Validation//
    ///////////////////

}
