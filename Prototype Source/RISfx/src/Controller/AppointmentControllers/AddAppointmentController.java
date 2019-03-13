package Controller.AppointmentControllers;

import Controller.Main;
import Model.Appointment;
import Model.ScheduleConflict;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Controller.databaseConnector;
import javafx.scene.control.cell.PropertyValueFactory;

public class AddAppointmentController implements Initializable {

    public static void setView() throws Exception{
        Main.setPopupWindow("AppointmentViews/addAppointment.fxml");
    }

    @FXML TextField patientIDField;
    @FXML DatePicker scheduleDate;
    @FXML ComboBox<String> procedureBox;
    @FXML TableView<Appointment> scheduleTime;
    @FXML TableColumn<Appointment, String> modCol;
    @FXML TableColumn<Appointment, String> techCol;
    @FXML TableColumn<Appointment, LocalTime> timeSlotCol;
    private int comboSelection = 0;
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
            }catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            updateTable();
        }catch (Exception e){e.printStackTrace();}

        //Procedure Section
        try {
            ObservableList<String> procedureList = Main.getProcedureList();
            //TODO: When sending selected procedure, split string and trim for id
            procedureBox.setItems(procedureList);
            procedureBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    comboSelection = Integer.parseInt(procedureBox.getValue().split(": ")[0]);
                    updateTable();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateTable() throws Exception{
        System.out.println("Table Updated");
        scheduleTime.setItems(FXCollections.observableArrayList());
        scheduleTime.setItems(generateTimeSlots(60));

        timeSlotCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalTime>("appointmentTime"));
        modCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("machineId"));
        modCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("technician"));
    }

    private ResultSet queryEmployeeSchedule() throws Exception{
        Connection conn = databaseConnector.getConnection();

        PreparedStatement employeeSchedule = conn.prepareStatement(
            "SELECT  CONCAT(employees.first_name, \" \", employees.last_name) AS employee_name, employees.employee_id, " +
                    "employee_schedule.start_time, employee_schedule.end_time, modality.machine_id " +
                    "FROM modality, employee_schedule " +
                    "INNER JOIN employees ON employee_schedule.employee_id=employees.employee_id " +
                    "WHERE " +
                    "employee_schedule.start_time BETWEEN ? and ? && " +
                    "modality.machine_id = ?"
        );
        employeeSchedule.setString(1, scheduleDate.getValue().toString()+ " 00:00:00");
        employeeSchedule.setString(2, scheduleDate.getValue().toString()+ " 20:00:00");
        employeeSchedule.setInt(3, comboSelection);
        return employeeSchedule.executeQuery();
    }

    private ResultSet queryConflicts(int employeeId) throws Exception{
        Connection conn = databaseConnector.getConnection();

        PreparedStatement conflicts = conn.prepareStatement(
                "SELECT appointments.appointment_id, appointments.appointment_date, appointments.appointment_time, " +
                        "procedures.procedure_length, procedures.procedure_name, CONCAT(employees.first_name, \" \", employees.last_name) AS employee_name, employees.employee_id, " +
                        "modality.machine_name, modality.machine_id " +
                        "FROM   appointments " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN modality ON appointments.machine_id=modality.machine_id " +
                        "WHERE " +
                        "appointments.appointment_date = ? &&" +
                        "   \temployees.employee_id = ?"
        );
        conflicts.setDate(1, Date.valueOf(scheduleDate.getValue()));
        conflicts.setInt(2, employeeId);
        return conflicts.executeQuery();
    }

    private ObservableList<Appointment> generateTimeSlots(int minuteIncrement) throws Exception{
        ResultSet employeeSchedule = queryEmployeeSchedule();

        ObservableList<Appointment> timeSlotList = FXCollections.observableArrayList();

        LocalTime employeeStartTime, employeeEndTime;
        int employeeId, machineId;
        String technician, machine;

        while(employeeSchedule.next()){
            employeeStartTime = employeeSchedule.getTime("start_time").toLocalTime();
            employeeEndTime = employeeSchedule.getTime("end_time").toLocalTime();
            employeeId = employeeSchedule.getInt("employee_id");
            technician = employeeSchedule.getString("employee_name");
            machineId = employeeSchedule.getInt("machine_id");

            //Check for all conflicts with current employee that is scheduled
            ResultSet rs = queryConflicts(employeeId);
            ArrayList<ScheduleConflict> conflicts = new ArrayList<>();
            while (rs.next()) {
                LocalDate cDate = rs.getDate("appointment_date").toLocalDate();
                LocalTime cTime = rs.getTime("appointment_time").toLocalTime();

                conflicts.add(new ScheduleConflict(
                        rs.getInt("procedure_length"),
                        LocalDateTime.of(cDate, cTime)
                ));
            }

            while (employeeStartTime.isBefore(employeeEndTime)) {
                for (ScheduleConflict scheduleConflict:
                        conflicts) {
                    if ((scheduleConflict.getConflictDateTime().plusMinutes(scheduleConflict.getConflictLength()*60).toLocalTime() == employeeStartTime) ||
                        (scheduleConflict.getConflictDateTime().minusMinutes(scheduleConflict.getConflictLength()*60).toLocalTime() == employeeStartTime) ||
                        (scheduleConflict.getConflictDateTime().plusMinutes(scheduleConflict.getConflictLength()*30).toLocalTime() == employeeStartTime) ||
                        (scheduleConflict.getConflictDateTime().minusMinutes(scheduleConflict.getConflictLength()*30).toLocalTime() == employeeStartTime)||
                        (scheduleConflict.getConflictDateTime().toLocalTime() == employeeStartTime)){

                        employeeStartTime = employeeStartTime.plusMinutes(scheduleConflict.getConflictLength()*60);
                        System.out.println("There was a conflict");
                    }
                }
                timeSlotList.add(new Appointment(
                    machineId,
                    employeeId,
                    Date.valueOf(employeeSchedule.getDate("start_time").toLocalDate()),
                    Time.valueOf(employeeStartTime)
                ));
                employeeStartTime = employeeStartTime.plusMinutes(minuteIncrement);
            }
        }

        return timeSlotList;
    }

}
