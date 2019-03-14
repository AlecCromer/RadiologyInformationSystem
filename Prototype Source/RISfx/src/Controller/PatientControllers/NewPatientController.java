package Controller.PatientControllers;

import Controller.Main;
import Controller.databaseConnector;
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


      ////////////////////
     //Database Queries//
    ////////////////////
    private int insertAddress() throws Exception{
        PreparedStatement insertAddress = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO address(street_name, city, state, zip)" +
                        "VALUES(?, ?, ?, ?)");
        return prepareAddress(insertAddress).executeUpdate();
    }

    private ResultSet queryAddress() throws Exception{
        PreparedStatement addressQuery = databaseConnector.getConnection().prepareStatement(
            "SELECT address_id FROM address " +
                "WHERE street_name = ? AND city = ? AND state = ? AND zip = ?");
        return prepareAddress(addressQuery).executeQuery();
    }

    private PreparedStatement prepareAddress(PreparedStatement statement) throws Exception{
        statement.setString(1, addressField.getText());
        statement.setString(2, cityField.getText());
        statement.setString(3, stateField.getText());
        statement.setInt(4, Integer.parseInt(zipField.getText()));
        return statement;
    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    public void submitNewPatient() throws Exception{
        if (validateForm()){
            Connection conn = databaseConnector.getConnection();

            if(insertAddress() == 1) {

                ResultSet addressSet = queryAddress();
                addressSet.next();
                int address_id = addressSet.getInt("address_id");

                PreparedStatement insertNewUser = conn.prepareStatement(
                        "INSERT INTO patient(patient_id, first_name, last_name, date_of_birth, sex, home_phone, email, insurance_number, policy_number, address_id, status, patient_medications_list)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)"
                );
                insertNewUser.setInt(1, Math.abs((new Random()).nextInt(50000)));
                insertNewUser.setString(2, fNameField.getText());
                insertNewUser.setString(3, lNameField.getText());
                insertNewUser.setString(4, dateFormatter(dobField.getValue()));
                insertNewUser.setString(5, sexField.getText());
                insertNewUser.setString(6, pNumberField.getText());
                insertNewUser.setString(7, emailField.getText());
                insertNewUser.setString(8, insuranceField.getText());
                insertNewUser.setString(9, policyField.getText());
                insertNewUser.setInt(10, address_id);
                insertNewUser.setString(11, "New Patient");

                int result = insertNewUser.executeUpdate();

                exitView();
            }
            else{
                System.out.println("Your form is invalid");
            }
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
