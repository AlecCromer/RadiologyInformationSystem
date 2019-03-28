package Model;

import Controller.databaseConnector;
import javafx.stage.Modality;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int appointmentId, procedureId, patientId, machineId, employeeId;
    private Date appointmentDate;
    private Time appointmentTime, patientSignIn, patientSignOut;
    private String refferalReason, Comments, patientFullName, patientStatus, dateTime, technician, machineName, procedureName;
    private Modality modality;
    private float balance;


      /////////////////////
     //Object Generators//
    /////////////////////
    public static Appointment generateAppointmentFocus(ResultSet resultSet) throws Exception{
        ResultSet patientInfo = Patient.queryPatientInfo(resultSet.getInt("patient_id"));
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
                resultSet.getString("patient_status"),
                String.format("%s - %s", format.format(resultSet.getDate("appointment_date")), timeFormat.format(resultSet.getTime("appointment_time"))),
                resultSet.getString("full_name"),
                resultSet.getString("procedure_name")
        );
    }


      ////////////////////
     //Database Queries//
    ////////////////////
    public static ResultSet queryAppointments()throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(employees.first_name, \" \", employees.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id")).executeQuery();
    }

    public static ResultSet queryAppointmentFocus(int appointmentId) throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(employees.first_name, \" \", employees.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "WHERE appointments.appointment_id = " + appointmentId)).executeQuery();
    }

    public static ResultSet queryWorkList(int employeeID) throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(employees.first_name, \" \", employees.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "WHERE appointments.employee_id="+employeeID)).executeQuery();
    }

    public static ResultSet queryBillingList() throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT billing.*, " +
                        "items.item_name, SUM(items.item_cost) AS 'appointment_total', " +
                        "appointments.appointment_date, appointments.patient_status, " +
                        "patient.first_name, patient.last_name " +
                        "FROM billing " +
                        "INNER JOIN appointments ON billing.appointment_id=appointments.appointment_id " +
                        "INNER JOIN items ON billing.item_id=items.item_id " +
                        "INNER JOIN patient ON appointments.patient_id=patient.patient_id " +
                        "GROUP BY appointment_id ASC"
        ).executeQuery();
    }


      ///////////////////
     //Getters/Setters//
    ///////////////////
      public int getAppointmentId() {
          return appointmentId;
      }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getProcedureId() {
        return procedureId;
    }
    public void setProcedure(int procedureId) {
        this.procedureId = procedureId;
    }

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getMachineId() {
        return machineId;
    }
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Time getPatientSignIn() {
        return patientSignIn;
    }
    public void setPatientSignIn(Time patientSignIn) {
        this.patientSignIn = patientSignIn;
    }

    public Time getPatientSignOut() {
        return patientSignOut;
    }
    public void setPatientSignOut(Time patientSignOut) {
        this.patientSignOut = patientSignOut;
    }

    public String getRefferalReason() {
        return refferalReason;
    }
    public void setRefferalReason(String refferalReason) {
        this.refferalReason = refferalReason;
    }

    public String getComments() {
        return Comments;
    }
    public void setComments(String comments) {
        Comments = comments;
    }

    public String getPatientFullName() {
        return patientFullName;
    }
    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public String getPatientStatus(){ return patientStatus; }
    public void setPatientStatus(String patientStatus){ this.patientStatus = patientStatus; }

    public String getDateTime(){ return dateTime; }
    public void setDateTime(String dateTime){ this.dateTime = dateTime; }

    public String getTechnician() {
        return technician;
    }
    public void setTechnician(String technician){ this.technician = technician; }

    public String getMachineName() {
        return machineName;
    }
    public void  setMachineName(String machineName){ this.machineName = machineName; }

    public String getProcedureName() {
        return procedureName;
    }

    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }

    ////////////////
     //Constructors//
    ////////////////
    public Appointment(){
        this.appointmentDate = null;
        this.appointmentTime = null;
        this.patientSignIn = null;
        this.patientSignOut = null;
        this.refferalReason = null;
        this.Comments = null;
        this.appointmentId = -1;
        this.procedureId = -1;
        this.patientId = -1;
        this.machineId = -1;
        this.employeeId = -1;
    }

    public Appointment(int appointmentId, String dateTime, Time patientSignIn, Time patientSignOut, String patientStatus) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.patientSignIn = patientSignIn;
        this.patientSignOut = patientSignOut;
        this.patientStatus = patientStatus;
    }

    public Appointment(int machineId, String machineName, int employeeId, String technician, Date appointmentDate, Time appointmentTime) {
        this.machineId          = machineId;
        this.machineName        = machineName;
        this.employeeId         = employeeId;
        this.technician         = technician;
        this.appointmentDate    = appointmentDate;
        this.appointmentTime    = appointmentTime;
    }

    public Appointment(int machineId, String machineName, int employeeId, String technician) {
        this.machineId          = machineId;
        this.machineName        = machineName;
        this.employeeId         = employeeId;
        this.technician         = technician;
    }

    public Appointment(int appointmentId, Date appointmentDate, String patientFullName, String patientStatus, float balance) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.patientFullName = patientFullName;
        this.balance = balance;
        this.patientStatus = patientStatus;
    }

    public Appointment(int appointmentId, int procedure, int patientId, String patientFullName, int machineId, int employeeId, Date appointmentDate,
                       Time appointmentTime, Time patientSignIn, Time patientSignOut, String refferalReason, String Comments, String patientStatus, String dateTime, String technician, String procedureName){
        this.appointmentDate    = appointmentDate;
        this.appointmentTime    = appointmentTime;
        this.patientSignIn      = patientSignIn;
        this.patientSignOut     = patientSignOut;
        this.refferalReason     = refferalReason;
        this.Comments           = Comments;
        this.appointmentId      = appointmentId;
        this.procedureId        = procedure;
        this.patientId          = patientId;
        this.machineId          = machineId;
        this.employeeId         = employeeId;
        this.patientStatus      = patientStatus;
        this.patientFullName    = patientFullName;
        this.dateTime           = dateTime;
        this.technician         = technician;
        this.procedureName      = procedureName;
    }
}
