package Controller.PatientControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;

public class NewPatientController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField     addressField, cityField, stateField, zipField;
    @FXML TextField     fNameField, lNameField,  pNumberField, sNumberField,
                        emailField, insuranceField, policyField, sexField;
    @FXML Button        patientSubmit;
    @FXML CheckBox      toggler;
    @FXML DatePicker    scheduleDate, dobField;
    @FXML TableView     scheduleTime;
    @FXML ComboBox      appointmentCBox;


      ////////////////
     //Initializers//
    ////////////////
    public void initialize(URL url, ResourceBundle arg1){
        dobField.setValue(LocalDate.of(1950, 1, 1));
    }

    public static void setView()throws Exception{
        Main.popup.setWidth(620);
        Main.popup.setHeight(300);
        Main.setPopupWindow("PatientViews/addPatient.fxml");
    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    public void submitNewPatient() throws Exception{
        if (validateForm()){
            Patient.insertNewPatient((new Patient(
                    fNameField.getText(),
                    lNameField.getText(),
                    sexField.getText(),
                    emailField.getText(),
                    dobField.getValue(),
                    Integer.parseInt(pNumberField.getText()),
                    Integer.parseInt(insuranceField.getText()),
                    Integer.parseInt(policyField.getText())
            )),
                    addressField.getText(),
                    cityField.getText(),
                    stateField.getText(),
                    zipField.getText()
            );

            Main.popup.close();
            Main.getOuter().setDisable(false);
            Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());
        }
        else{

        }
    }

    public void toggleSchedule(){
        if (toggler.isSelected()){
            scheduleDate.setVisible(true);
            scheduleTime.setVisible(true);
            appointmentCBox.setVisible(true);
            Main.popup.setHeight(Main.popup.getHeight() + 260);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() + 265);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() + 18);
        }
        else {
            scheduleDate.setVisible(false);
            scheduleTime.setVisible(false);
            appointmentCBox.setVisible(false);
            Main.popup.setHeight( Main.popup.getHeight() - 260);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() - 265);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() - 18);
        }
    }

    private void exitView() throws Exception{
        Main.popup.close();
        Main.getOuter().setDisable(false);
        PatientListController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private String dateFormatter(LocalDate date){
          DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
          return date.format(format);
      }

    private boolean validateForm(){
        //TODO: Implement actual form validation

        return true;
    }
}
