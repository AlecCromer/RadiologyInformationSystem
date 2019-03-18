package Controller.TechControllers;

import Controller.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.LocalTime;
import java.util.ArrayList;

public class TechEntryController {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
      @FXML
      TextField pNameField, appointmentIDField, appointmentDateField,
              appointmentTimeField, signInField, signOutField;
    @FXML
    ComboBox<String> checkInOutBox;
    @FXML
    Button submitCheckInOut;
    private String comboSelection = "";

      ////////////////
     //Initializers//
    ////////////////


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
