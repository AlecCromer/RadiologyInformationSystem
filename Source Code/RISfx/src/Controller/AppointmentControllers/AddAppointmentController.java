package Controller.AppointmentControllers;

import Controller.Main;
import Model.*;
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
import java.util.Iterator;
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

    /**
     * Executes setPopupWindow() in main class to load addAppointment.fxml as a popup.
     * @throws Exception
     */
    public static void setView() throws Exception{
        Main.popup.setHeight(500);
        Main.popup.setWidth(600);
        Main.setPopupWindow("AppointmentViews/addAppointment.fxml");
    }

    /**
     * This logic operates the popup window calendar selection by populating the FXML fields.
     * To generate the procedure list for the procedure box it calls Procedure.getProcedureList()
     * Splits the procedure ID out of the name as combo selection for use in timing,
     *      e.g. 1:xray passes 1 in combo selection
     * @param url
     * @param arg1
     */
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

    /**
     * This is called by the intialize method to populate the tableview of the fxml.
     * @throws Exception
     */
    private void updateTable() throws Exception{
        //scheduleTime.setItems(FXCollections.observableArrayList());
        scheduleTime.setItems(generateTimeSlots(60));
        timeSlotCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalTime>("appointmentTime"));
        techCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("technician"));
        modCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("machineName"));
    }


      ///////////////////
     //List Generators//
    ///////////////////

    /**
     * Generates timeslots over minute increment.
     * Uses Employee.queryEmployeeSchedule with the date set above to get employee schedules
     * Generates a list of appointment objects for all times each employee is available according to their schedule and adds to list
     * Uses AddAppointmentController.generateConflictList() to find existing scheduled appointments, comboselection to
     *      get the time that the previously scheduled appointments take, then removes all times that are already taken from list
     * @param minuteIncrement
     * @return
     * @throws Exception
     */
    private ObservableList<Appointment> generateTimeSlots(int minuteIncrement) throws Exception{
        ResultSet employeeSchedule = Employee.queryEmployeeSchedule(scheduleDate.getValue(), comboSelection);

        ObservableList<Appointment> timeSlotList = FXCollections.observableArrayList();

        LocalTime employeeStartTime, employeeEndTime;

        ArrayList<ScheduleConflict> conflicts = new ArrayList<>();

        while(employeeSchedule.next()){
            employeeStartTime   = employeeSchedule.getTime("start_time").toLocalTime();
            employeeEndTime     = employeeSchedule.getTime("end_time").toLocalTime();

           while (employeeStartTime.isBefore(employeeEndTime) || employeeStartTime.equals(employeeEndTime)) {
                //Generate a time slot and add it to the list
                Appointment timeSlot = new Appointment(employeeSchedule.getInt("machine_id"), employeeSchedule.getString("machine_name"),
                        employeeSchedule.getInt("employee_id"), employeeSchedule.getString("employee_name"));
                timeSlot.setAppointmentTime(Time.valueOf(employeeStartTime));
                timeSlot.setAppointmentDate(Date.valueOf(employeeSchedule.getDate("start_time").toLocalDate()));
                timeSlotList.add(timeSlot);

                //increment
                employeeStartTime = employeeStartTime.plusMinutes(minuteIncrement);
            }

           conflicts.addAll(generateConflictList(employeeSchedule.getInt("employee_id")));


            for (int i = 0; i < timeSlotList.toArray().length; i++) {
                for (ScheduleConflict confl: conflicts) {
                    if(confl.getConflictDateTime().equals(LocalDateTime.of(
                            timeSlotList.get(i).getAppointmentDate().toLocalDate(),
                            timeSlotList.get(i).getAppointmentTime().toLocalTime())) && (
                                    confl.getTechnicianID() == employeeSchedule.getInt("employee_id") ||
                                            confl.getMachineID() == Modality.queryMachineIdByProcedureType(comboSelection))
                    ){
                        try {
                            int j = (i - Procedure.queryProcedureLength(comboSelection)+1) < 0 ? 0 : (i - Procedure.queryProcedureLength(comboSelection)+1);
                            timeSlotList.remove(j, i + confl.getConflictLength());
                        }
                        catch (Exception e){
                            System.out.println("oof");
                        }
                    }
                }
            }
        }
        return timeSlotList;
    }

    /**
     * Uses ScheduleConflict.queryConflicts() with the date and assigned employeeID to return list of conflicting
     * appointments with above list
     * @param employeeId Employee ID
     * @return
     * @throws Exception
     */
    private ArrayList<ScheduleConflict> generateConflictList(int employeeId) throws Exception{
        //Check for all conflicts with current employee that is scheduled
        ResultSet rs = ScheduleConflict.queryConflicts(scheduleDate.getValue(), employeeId);
        ArrayList<ScheduleConflict> conflicts = new ArrayList<>();
        while (rs.next()) {
            LocalDate cDate = rs.getDate("appointment_date").toLocalDate();
            LocalTime cTime = rs.getTime("appointment_time").toLocalTime();

            conflicts.add(new ScheduleConflict(
                    rs.getInt("procedure_length"),
                    LocalDateTime.of(cDate, cTime),
                    rs.getInt("employee_id"),
                    rs.getInt("machine_id")
            ));
        }
        return conflicts;
    }


      //////////////////
     //Button Methods//
    //////////////////
    @FXML
    /**
     * Creates appointmentToSubmit as appointment object. Then sets each value.
     * Then tries to execute Appointment.submitNewAppointment() on the appointment object.
     * If this fails, it outlines the patientIDField on the addAppointment.FXML in red.
     */
    private void submitNewAppointment() throws Exception{
        if (validateAppointment()) {
            Appointment appointmentToSubmit = scheduleTime.getSelectionModel().getSelectedItem();
            appointmentToSubmit.setProcedure(comboSelection);
            appointmentToSubmit.setPatientId(Integer.parseInt(patientIDField.getText()));
            try {
                Appointment.submitNewAppointment(appointmentToSubmit);
                exitView();
            }catch (Exception e){
                patientIDField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }

        }
    }


      ///////////////////
     //Form Validation//
    ///////////////////

    /**
     * Makes sure that the appointment has an available machine and technician
     * @return true if allowed
     */
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

    /**
     * Closes the addAppointment popup and removes the background blur effect
     * @throws Exception
     */
    private void exitView() throws Exception{
        Main.popup.close();
        Main.getOuter().setEffect(null);
        Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());
    }
}
