package Controller.AppointmentControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import Model.Employee;
import Model.Procedure;
import Model.ScheduleConflict;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField                             patientIDField;
    @FXML DatePicker                            scheduleDate;
    @FXML ComboBox<String>                      procedureBox;
    @FXML TableView<Appointment>                scheduleTime;
    @FXML TableColumn<Appointment, String>      modCol;
    @FXML TableColumn<Appointment, String>      techCol;
    @FXML TableColumn<Appointment, LocalTime>   timeSlotCol;

    private int comboSelection = 0;


      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.popup.setHeight(500);
        Main.popup.setWidth(600);
        Main.setPopupWindow("AppointmentViews/addAppointment.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        //Patient ID Section
        if((Main.getPatientFocus().getPatientID() != -1)){
            patientIDField.setText(String.valueOf(Main.getPatientFocus().getPatientID()));
        }
        //Date Picker Section
        scheduleDate.setValue(LocalDate.now());
        scheduleDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            try {
                updateTable();
            }catch (Exception e) { e.printStackTrace(); }
        });

        try {
            updateTable();
        }catch (Exception e){e.printStackTrace();}

        //Procedure Section
        try {
            ObservableList<String> procedureList = Procedure.getProcedureList();
            procedureBox.setItems(procedureList);
            procedureBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    comboSelection = Integer.parseInt(procedureBox.getValue().split(": ")[0]);
                    updateTable();
                }catch (Exception e) { e.printStackTrace(); }
            });
        }
        catch (Exception e){ e.printStackTrace(); }
        scheduleTime.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{

                        submitNewAppointment();

                }catch(Exception e){ e.printStackTrace(); }
            } });
    }

    private void updateTable() throws Exception{
        scheduleTime.setItems(FXCollections.observableArrayList());
        scheduleTime.setItems(generateTimeSlots(60));
        timeSlotCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalTime>("appointmentTime"));
        techCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("technician"));
        modCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("machineName"));
    }


      ///////////////////
     //List Generators//
    ///////////////////
    private ObservableList<Appointment> generateTimeSlots(int minuteIncrement) throws Exception{
        ResultSet employeeSchedule = Employee.queryEmployeeSchedule(scheduleDate.getValue(), comboSelection);

        ObservableList<Appointment> timeSlotList = FXCollections.observableArrayList();

        LocalTime employeeStartTime, employeeEndTime;

        while(employeeSchedule.next()){
            employeeStartTime   = employeeSchedule.getTime("start_time").toLocalTime();
            employeeEndTime     = employeeSchedule.getTime("end_time").toLocalTime();

            ArrayList<ScheduleConflict> conflicts = generateConflictList(employeeSchedule.getInt("employee_id"));

            while (employeeStartTime.isBefore(employeeEndTime)) {
                for (ScheduleConflict scheduleConflict:
                        conflicts) {
                    if ((scheduleConflict.getConflictDateTime().plusMinutes(scheduleConflict.getConflictLength()*60).toLocalTime() == employeeStartTime) ||
                            (scheduleConflict.getConflictDateTime().plusMinutes((scheduleConflict.getConflictLength()*60) + 30).toLocalTime() == employeeStartTime) ||
                            (scheduleConflict.getConflictDateTime().plusMinutes((scheduleConflict.getConflictLength()*60) - 30).toLocalTime() == employeeStartTime) ||
                            (scheduleConflict.getConflictDateTime().toLocalTime() == employeeStartTime)){
                        employeeStartTime = employeeStartTime.plusMinutes(scheduleConflict.getConflictLength()*60);
                    }
                }
                Appointment timeSlot = new Appointment(employeeSchedule.getInt("machine_id"), employeeSchedule.getString("machine_name"),
                        employeeSchedule.getInt("employee_id"), employeeSchedule.getString("employee_name"));
                timeSlot.setAppointmentTime(Time.valueOf(employeeStartTime));
                timeSlot.setAppointmentDate(Date.valueOf(employeeSchedule.getDate("start_time").toLocalDate()));
                timeSlotList.add(timeSlot);
                employeeStartTime = employeeStartTime.plusMinutes(minuteIncrement);
            }
        }
        return timeSlotList;
    }

    private ArrayList<ScheduleConflict> generateConflictList(int employeeId) throws Exception{
        //Check for all conflicts with current employee that is scheduled
        ResultSet rs = ScheduleConflict.queryConflicts(scheduleDate.getValue(), employeeId);
        ArrayList<ScheduleConflict> conflicts = new ArrayList<>();
        while (rs.next()) {
            LocalDate cDate = rs.getDate("appointment_date").toLocalDate();
            LocalTime cTime = rs.getTime("appointment_time").toLocalTime();

            conflicts.add(new ScheduleConflict(
                    rs.getInt("procedure_length"),
                    LocalDateTime.of(cDate, cTime)
            ));
        }
        return conflicts;
    }


      //////////////////
     //Button Methods//
    //////////////////
    @FXML
    private void submitNewAppointment() throws Exception{
        if (validateAppointment()) {
            Appointment appointmentToSubmit = scheduleTime.getSelectionModel().getSelectedItem();
            appointmentToSubmit.setProcedure(comboSelection);
            appointmentToSubmit.setPatientId(Integer.parseInt(patientIDField.getText()));

            Connection conn = databaseConnector.getConnection();
            PreparedStatement update = conn.prepareStatement(
                    "INSERT INTO `appointments` (`patient_id`, `appointment_date`, `appointment_time`, `procedure_id`, `machine_id`, `employee_id`) " +
                            "VALUES (?, ?, ?, ?, ?, ?);");
            update.setInt(1, appointmentToSubmit.getPatientId());
            update.setDate(2, appointmentToSubmit.getAppointmentDate());
            update.setTime(3, appointmentToSubmit.getAppointmentTime());
            update.setInt(4, appointmentToSubmit.getProcedureId());
            update.setInt(5, appointmentToSubmit.getMachineId());
            update.setInt(6, appointmentToSubmit.getEmployeeId());

            update.executeUpdate();
            exitView();
        }
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private Boolean validateAppointment(){
        Appointment selectedTimeSlot = scheduleTime.getSelectionModel().getSelectedItem();

        try{
            selectedTimeSlot.setPatientId(Integer.parseInt(patientIDField.getText()));
        }catch (Exception e){return false;}

        //Validate everything but the table
        if((scheduleDate.getValue().toString() != "" && !(scheduleDate.getValue().isBefore(LocalDate.now()))) &&
            (comboSelection != 0)){

            selectedTimeSlot.setProcedure(comboSelection);
            //Table rows are appointment objects; Validate the object here
            if ((selectedTimeSlot.getAppointmentDate() != null) &&
                (selectedTimeSlot.getAppointmentTime() != null) &&
                (selectedTimeSlot.getProcedureId() > 0) &&
                (selectedTimeSlot.getPatientId() > 0) &&
                (selectedTimeSlot.getMachineId() > 0) &&
                (selectedTimeSlot.getEmployeeId() > 0)) {

                return true;
            }
        }
        System.out.println("Something was not set");
        return false;
    }

    private void exitView() throws Exception{
        Main.popup.close();
        Main.getOuter().setDisable(false);
        Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());
    }
}
