package Model;

import Controller.databaseConnector;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class Patient {

    private String  firstname, lastname, sex, email, address;
    private LocalDate dob;
    private int phoneNumber, patientID, insuranceNumber;
    private int policyNumber;
    private ArrayList<Appointment> AppointmentList;

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getPatientID() {
        return patientID;
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getInsuranceNumber(){return insuranceNumber;}
    public void setInsuranceNumber(int insuranceNumber){this.insuranceNumber = insuranceNumber;}

    public int getPolicyNumber() {
        return this.policyNumber;
    }
    public void setPolicyNumber(int policyNumber) {
        this.policyNumber = policyNumber;
    }

    public ArrayList<Appointment> getAppointmentList() {
        return AppointmentList;
    }
    public void setAppointmentList(ArrayList<Appointment> appointmentList) {
        AppointmentList = appointmentList;
    }


      ///////////////
     //Constructor//
    ///////////////
    public Patient(){
        this.patientID          = -1;
        this.firstname          = null;
        this.lastname           = null;
        this.dob                = null;
        this.sex                = null;
        this.phoneNumber        = -1;
        this.email              = null;
        this.address            = null;
        this.insuranceNumber    = -1;
        this.policyNumber       = -1;
        this.AppointmentList    = new ArrayList<>();
    }

    public Patient(int patientID, String firstname, String lastname, int phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.patientID = patientID;
    }

    public Patient(int PatientID, String firstname, String lastname, LocalDate DoB, String Sex, int phoneNumber, String email, int insuranceNumber, int policyNumber, String address ){
        this.patientID          = PatientID;
        this.firstname          = firstname;
        this.lastname           = lastname;
        this.dob                = DoB;
        this.sex                = Sex;
        this.phoneNumber        = phoneNumber;
        this.email              = email;
        this.insuranceNumber    = insuranceNumber;
        this.policyNumber       = policyNumber;
        this.address            = address;
        this.AppointmentList    = new ArrayList<>();
    }


    public static ResultSet queryPatientInfo(int patientID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT first_name, last_name, status FROM patient " +
                        "WHERE patient_id = " + patientID).executeQuery();
    }
}
