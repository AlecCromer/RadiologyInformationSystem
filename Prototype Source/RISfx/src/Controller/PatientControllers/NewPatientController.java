package Controller.PatientControllers;

import Controller.Controller;
import Controller.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Controller.databaseConnector;

public class NewPatientController extends Controller {

    @FXML Button patientSubmit;
    @FXML CheckBox toggler;
    @FXML DatePicker scheduleDate;
    @FXML TableView scheduleTime;
    @FXML ComboBox appointmentCBox;
    public void toggleSchedule(){
        if (toggler.isSelected()){
            scheduleDate.setVisible(true);
            scheduleTime.setVisible(true);
            appointmentCBox.setVisible(true);
            Main.popup.setHeight(Main.popup.getHeight() + 250);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() + 265);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() + 18);
        }
        else {
            scheduleDate.setVisible(false);
            scheduleTime.setVisible(false);
            appointmentCBox.setVisible(false);
            Main.popup.setHeight( Main.popup.getHeight() - 240);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() - 265);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() - 18);
        }
    }

    public static void setView()throws Exception{
        Main.setPopupWindow("PatientViews/addPatient.fxml");
    }

    @FXML TextField addressField, cityField, stateField, zipField;
    @FXML TextField fNameField, lNameField, dobField, pNumberField,
            sNumberField, emailField, insuranceField, policyField, sexField;
    public void submitNewPatient() throws Exception{
        if (validateForm()){
            Connection conn = databaseConnector.getConnection();

            PreparedStatement insertAddress = conn.prepareStatement(
                    "INSERT INTO address(street_name, city, state, zip)" +
                            "VALUES(?, ?, ?, ?)");
            insertAddress.setString(1, addressField.getText());
            insertAddress.setString(2, cityField.getText());
            insertAddress.setString(3, stateField.getText());
            insertAddress.setInt(4, Integer.parseInt(zipField.getText()));

            int address_id = insertAddress.executeUpdate();

            PreparedStatement insertNewUser = conn.prepareStatement(
                    "INSERT INTO patient(first_name, last_name,  address_id, home_phone, second_phone, email, insurance_number, policy_number, status, sex)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            insertNewUser.setString(1, fNameField.getText());
            insertNewUser.setString(2, lNameField.getText());
            //insertNewUser.setString(3, dobField.getText());
            insertNewUser.setInt(3, address_id);
            insertNewUser.setString(4, pNumberField.getText());
            insertNewUser.setString(5, sNumberField.getText());
            insertNewUser.setString(6, emailField.getText());
            insertNewUser.setString(7, insuranceField.getText());
            insertNewUser.setString(8, policyField.getText());
            insertNewUser.setInt(9, 1);
            insertNewUser.setString(10, sexField.getText());
            int result = insertNewUser.executeUpdate();

            exitView();
        }
        else{

        }
    }

    private void exitView() throws Exception{
        Main.popup.close();
        Main.getOuter().setDisable(false);
        PatientListController.setPatientList();
    }

    private boolean validateForm(){


        return true;
    }
}
