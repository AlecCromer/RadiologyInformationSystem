package Controller.AppointmentControllers;

import Controller.databaseConnector;
import Controller.Main;
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

    //sets the View
    public static void setView()throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentList.fxml");
    }

    @FXML TableView<Appointment> AppointmentList;
    @FXML TableColumn<Appointment, Integer> appointmentID;
    @FXML TableColumn<Appointment, String> patientFullName, DateTime, ProcedureType, Technician, Status, Balance;

    //Use to initialize the table
    public void initialize(URL url, ResourceBundle arg1) {
        AppointmentListFill();
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

    private void AppointmentListFill() {
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
        ProcedureType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("procedure"));
        Technician.setCellValueFactory(new PropertyValueFactory<Appointment, String>("employeeId"));
        Status.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));
    }

    //@SuppressWarnings(value = "Duplicates")
    private ObservableList<Appointment> getAppointmentList() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet resultSet = (databaseConnector.getConnection().prepareStatement("select * FROM appointments")).executeQuery()){
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
    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();

        Connection conn = databaseConnector.getConnection();

        ResultSet rs = (conn.prepareStatement("select * FROM appointments WHERE appointment_id = " + appointmentId)).executeQuery();
        rs.next();
        Main.setAppointmentFocus(generateAppointment(rs));
    }

    public Appointment generateAppointment(ResultSet resultSet) throws Exception{
        Connection conn = databaseConnector.getConnection();

        ResultSet patientInfo = conn.prepareStatement(
                "SELECT first_name, last_name, status FROM patient " +
                        "WHERE patient_id = " + resultSet.getInt("patient_id")).executeQuery();
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
                String.format("%s - %s", format.format(resultSet.getDate("appointment_date")), timeFormat.format(resultSet.getTime("appointment_time")))
        );
    }

    /////////////////////////
     //Button Function Calls//
    /////////////////////////
    public void setAppointmentView(ActionEvent actionEvent) throws Exception{
        AppointmentViewController.setView();
    }

    //Reset the Patient Focus when pressing this button to clear the patient ID Field
    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        Main.setPatientFocus(new Patient());
        AddAppointmentController.setView();
    }
}
