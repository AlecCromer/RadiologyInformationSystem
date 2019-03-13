package Model;

import javafx.stage.Modality;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private int appointmentId, procedure, patientId, machineId, employeeId;
    private Date appointmentDate;
    private Time appointmentTime, patientSignIn, patientSignOut;
    private String refferalReason, Comments, patientFullName, patientStatus, dateTime, room;
    private Modality modality;

    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getProcedure() {
        return procedure;
    }
    public void setProcedure(int procedure) {
        this.procedure = procedure;
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

    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public Modality getModality() {
        return modality;
    }
    public void setModality(Modality modality) {
        this.modality = modality;
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
        this.procedure = -1;
        this.patientId = -1;
        this.machineId = -1;
        this.employeeId = -1;
    }

    public Appointment(int machineId, int employeeId, Date appointmentDate, Time appointmentTime) {
        this.machineId = machineId;
        this.employeeId = employeeId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.modality = modality;
    }

    public Appointment(int appointmentId, int procedure, int patientId, String patientFullName, int machineId, int employeeId, Date appointmentDate,
                       Time appointmentTime, Time patientSignIn, Time patientSignOut, String refferalReason, String Comments, String patientStatus, String dateTime){
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientSignIn = patientSignIn;
        this.patientSignOut = patientSignOut;
        this.refferalReason = refferalReason;
        this.Comments = Comments;
        this.appointmentId = appointmentId;
        this.procedure = procedure;
        this.patientId = patientId;
        this.machineId = machineId;
        this.employeeId = employeeId;
        this.patientStatus = patientStatus;
        this.patientFullName = patientFullName;
        this.dateTime = dateTime;
    }

}
