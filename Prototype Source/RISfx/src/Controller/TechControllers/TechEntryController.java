package Controller.TechControllers;

import Controller.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TechEntryController {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
      @FXML
      TextField pNameField, appointmentIDField, appointmentDateField,
              appointmentTimeField, signInField, signOutField;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
          Main.setCenterPane("TechViews/TechEntry.fxml");
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
      public void setBackPage() throws Exception {
          Main.setBackPage();
      }

      ///////////////////
     //Form Validation//
    ///////////////////
    private void comboBoxFill() throws Exception{

    }
}
