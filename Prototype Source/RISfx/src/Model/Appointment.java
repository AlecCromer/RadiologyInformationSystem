package Model;

import javafx.stage.Modality;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private int appointmentId, procedureId, patientId, machineId, employeeId;
    private Date appointmentDate;
    private Time appointmentTime, patientSignIn, patientSignOut;
    private String refferalReason, Comments, patientFullName, patientStatus, dateTime, room, technician, machineName, procedureName;
    private Modality modality;

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

    ///////////////
     //Constructor//
    ///////////////
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
