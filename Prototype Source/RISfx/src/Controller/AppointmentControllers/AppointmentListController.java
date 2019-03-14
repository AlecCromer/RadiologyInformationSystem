package Controller.AppointmentControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AppointmentListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TableView<Appointment>            AppointmentList;
    @FXML TableColumn<Appointment, Integer> appointmentID;
    @FXML TableColumn<Appointment, String>  patientFullName, DateTime, ProcedureType, Technician, Status, Balance;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView()throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
        AppointmentList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendAppointmentToView(AppointmentList.getSelectionModel().getSelectedItem());
                    AppointmentViewController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateTable() {
        try {

            AppointmentList.setItems(getAppointmentList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        patientFullName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientFullName"));
        DateTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
        ProcedureType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("procedureName"));
        Technician.setCellValueFactory(new PropertyValueFactory<Appointment, String>("technician"));
        Status.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));
    }


      ////////////////////
     //Database Queries//
    ////////////////////
    private ResultSet queryAppointments()throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(employees.first_name, \" \", employees.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id")).executeQuery();
    }

    private ResultSet queryPatientInfo(int patientID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT first_name, last_name, status FROM patient " +
                        "WHERE patient_id = " + patientID).executeQuery();
    }

    private ResultSet queryAppointmentFocus(int appointmentId) throws Exception{
        Connection conn = databaseConnector.getConnection();

        return (conn.prepareStatement(
                "SELECT appointments.*, CONCAT(employees.first_name, \" \", employees.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "WHERE appointments.appointment_id = " + appointmentId)).executeQuery();
    }


      ///////////////////
     //List Generators//
    ///////////////////
    private ObservableList<Appointment> getAppointmentList() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet resultSet = queryAppointments()){
            while (resultSet.next()){
                appointments.add(generateAppointment(resultSet));
            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return appointments;
    }

    public Appointment generateAppointment(ResultSet resultSet) throws Exception{
        ResultSet patientInfo = queryPatientInfo(resultSet.getInt("patient_id"));
        patientInfo.next();
        String patientFullName = patientInfo.getString("first_name") + " " + patientInfo.getString("last_name");

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");

        return new Appointment(
            resultSet.getInt("appointment_id"),
            resultSet.getInt("procedure_id"),
            resultSet.getInt("patient_id"),
            patientFullName,
            resultSet.getInt("machine_id"),
            resultSet.getInt("employee_id"),
            resultSet.getDate("appointment_date"),
            resultSet.getTime("appointment_time"),
            resultSet.getTime("patient_sign_in_time"),
            resultSet.getTime("patient_sign_out_time"),
            resultSet.getString("reason_for_referral"),
            resultSet.getString("special_comments"),
            patientInfo.getString("status"),
            String.format("%s - %s", format.format(resultSet.getDate("appointment_date")), timeFormat.format(resultSet.getTime("appointment_time"))),
            resultSet.getString("full_name"),
            resultSet.getString("procedure_name")
        );
    }


      //////////////////
     //Button Methods//
    //////////////////
    public void setAppointmentView(ActionEvent actionEvent) throws Exception{
        AppointmentViewController.setView();
    }

    //Reset the Patient Focus when pressing this button to clear the patient ID Field in next view
    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        Main.setPatientFocus(new Patient());
        AddAppointmentController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();
        ResultSet rs = queryAppointmentFocus(appointmentId);
        rs.next();
        Main.setAppointmentFocus(generateAppointment(rs));
    }
}
