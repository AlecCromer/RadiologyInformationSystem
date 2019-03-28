package Controller.PatientControllers;

import Controller.AppointmentControllers.AddAppointmentController;
import Controller.AppointmentControllers.AppointmentViewController;
import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.swing.text.DateFormatter;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;

public class PatientViewController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField                     fNameField,    lNameField,      pNumberField,
                                        addressField,  dobField,        sNumberField,
                                        emailField,    InsuranceField,  balanceField, policyField;
    @FXML Button                        EditPatientInfoButton;
    @FXML TableView<Appointment>        patientAppointments;
    @FXML
    TableColumn<Appointment, Time>      patientSignInTime, patientSignOutTime;
    @FXML
    TableColumn<Appointment, Integer>   appointmentID;
    @FXML
    TableColumn<Appointment, String>    patientStatus, appointmentDate;
    private boolean EditPatientLock = false;


      ////////////////
     //Initializers//
    ////////////////
    public static void setView()throws Exception{
        Main.setCenterPane("PatientViews/PatientView.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        fNameField.setText(Main.getPatientFocus().getFirstname());
        lNameField.setText(Main.getPatientFocus().getLastname());
        pNumberField.setText(String.valueOf(Main.getPatientFocus().getPhoneNumber()));
        addressField.setText(Main.getPatientFocus().getAddress());
        dobField.setText(dateFormatter(Main.getPatientFocus().getDob()));
        emailField.setText(Main.getPatientFocus().getEmail());
        InsuranceField.setText(String.valueOf(Main.getPatientFocus().getInsuranceNumber()));
        policyField.setText(String.valueOf(Main.getPatientFocus().getPolicyNumber()));
        updateAppointmentTable();
        patientAppointments.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendAppointmentToView(patientAppointments.getSelectionModel().getSelectedItem());
                    AppointmentViewController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateAppointmentTable(){
        try {
            patientAppointments.setItems(generatePatientAppointmentList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        appointmentDate.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
        patientSignInTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("patientSignIn"));
        patientSignOutTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("patientSignOut"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
    }

      ////////////////////
     //Database Queries//
    ////////////////////
    private ResultSet queryPatientAppointments(int patientId) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.appointment_id, appointments.appointment_date, appointments.patient_sign_in_time, appointments.patient_sign_out_time, appointments.patient_status " +
                        "FROM appointments " +
                        "WHERE appointments.patient_id = " + patientId
        ).executeQuery();
      }

    private ResultSet queryAppointmentFocus(int appointmentId) throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(employees.first_name, \" \", employees.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "WHERE appointments.appointment_id = " + appointmentId)).executeQuery();
    }

      ///////////////////
     //List Generators//
    ///////////////////
    private ObservableList<Appointment> generatePatientAppointmentList() throws Exception{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet rs = queryPatientAppointments(Main.getPatientFocus().getPatientID());){
            while (rs.next()){
                appointments.add(generateAppointment(rs));
            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return appointments;
    }

    private Appointment generateAppointment(ResultSet rs) throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String date = format.format(rs.getDate("appointment_date"));
        return new Appointment(
                rs.getInt("appointment_id"),
                date,
                rs.getTime("patient_sign_in_time"),
                rs.getTime("patient_sign_out_time"),
                rs.getString("patient_status")
        );
    }


      //////////////////
     //Button Methods//
    //////////////////
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

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private String dateFormatter(LocalDate date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(format);
    }

    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();
        ResultSet rs = queryAppointmentFocus(appointmentId);
        rs.next();
        Main.setAppointmentFocus(Appointment.generateAppointmentFocus(rs));
    }
}
