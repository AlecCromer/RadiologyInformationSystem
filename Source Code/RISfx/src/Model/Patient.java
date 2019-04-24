package Model;

import Controller.Main;
import Controller.databaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Patient {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    private String firstname;
    private String lastname;
    private String sex;
    private String email;
    private String address;
    private String phoneNumber;
    private String fullName;
    private String insuranceNumber;
    private String policyNumber;
    private String patientStatus;

    private LocalDate dob;

    private float balance;
    private int patientID;
    private ArrayList<Appointment> AppointmentList;


    ////////////////////
    //Database Queries//
    ////////////////////

    /**
     * Returns All patient records.
     * Used in PatientListController.getPatientList()
     * @return
     * @throws Exception
     */
    public static ResultSet queryAllPatients() throws Exception{
        ResultSet resultSet = databaseConnector.getConnection().prepareStatement(
                "select * FROM patient"
        ) .executeQuery();

        return resultSet;
    }

    /**
     * Returns all patient records where patient ID matches the given value.
     * Used in PatientListController.sendPatientToView(Patient)
     * Used in Appointment.generateAppointmentFocus()
     * Used in Appointment.setAddress()
     * @param patientID
     * @return
     * @throws Exception
     */
    public static ResultSet queryPatientInfo(int patientID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT * FROM patient " +
                        "WHERE patient_id = " + patientID).executeQuery();
    }

    /**
     * From records in the appointment table where patient ID is equal to the one given,
     *      selects appointment id, appointment date, sign in time, sign out time, and patient status
     * Used in PatientViewController.generatePatientAppointmentList()
     * @param patientId
     * @return
     * @throws Exception
     */
    public static ResultSet queryPatientAppointments(int patientId) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.appointment_id, appointments.appointment_date, appointments.patient_sign_in_time, appointments.patient_sign_out_time, appointments.patient_status " +
                        "FROM appointments " +
                        "WHERE appointments.patient_id = " + patientId
        ).executeQuery();
    }

    /**
     * I believe this is a depreciated method used for populating the billinglist from the patient table in the database, as opposed to the appointment table, in case you want
     * to bill for an entire patient balance instead of on a per-appointment basis.
     *
     * It selects patient id, first name, last name, status, balance, street, city, state, and zip code from the patient table and address table,
     * then left joins the information from the address table on patient table address ID = address table address ID
     *
     * No usages
     * @return
     * @throws Exception
     */
    public static ResultSet queryInfoForBillingList() throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT patient.patient_id, patient.first_name, patient.last_name, patient.status, patient.balance, address.street_name, address.city, address.state, address.zip " +
                        "FROM `patient` " +
                        "LEFT JOIN `address` ON patient.address_id = address.address_id"
        ).executeQuery();
    }

    /**
     * Inserts a new patient to the database.
     *
     * NOTE, PATIENT ID COLLISIONS ARE POSSIBLE VIA ASSIGNMENT METHOD
     *             patientID = Math.abs((new Random()).nextInt(50000));
     *
     * Used in NewPatientController.submitToDB()
     * Used in ReferralFormController.submitNewReferral()
     *
     * @param patientToInsert
     * @param address
     * @param city
     * @param state
     * @param zip
     * @return
     * @throws Exception
     */
    public static int insertNewPatient(Patient patientToInsert, String address, String city, String state, String zip) throws Exception {
        Connection conn = databaseConnector.getConnection();
        int patientID = -1;

        //this may be the problem brisaac
        if (insertAddress(address, city, state, zip) == 1) {
            ResultSet addressSet = queryAddress(address, city, state, zip);
            addressSet.next();
            int address_id = addressSet.getInt("address_id");

            PreparedStatement insertNewUser = conn.prepareStatement(
                    "INSERT INTO patient(patient_id, first_name, last_name, date_of_birth, sex, home_phone, email, insurance_number, policy_number, address_id, status, patient_medications_list)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)"
            );

            patientID = Math.abs((new Random()).nextInt(50000));

            insertNewUser.setInt(1, patientID);
            insertNewUser.setString(2, patientToInsert.getFirstname());
            insertNewUser.setString(3, patientToInsert.getLastname());
            insertNewUser.setString(4, patientToInsert.getDob().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            insertNewUser.setString(5, patientToInsert.getSex());
            insertNewUser.setString(6, String.valueOf(patientToInsert.getPhoneNumber()));
            insertNewUser.setString(7, patientToInsert.getEmail());
            insertNewUser.setString(8, String.valueOf(patientToInsert.getInsuranceNumber()));
            insertNewUser.setString(9, String.valueOf(patientToInsert.getPolicyNumber()));
            insertNewUser.setInt(10, address_id);
            insertNewUser.setString(11, "New Patient");
            insertNewUser.executeUpdate();
        }
        return patientID;
    }

    /**
     * Inserts an address to the address table.
     *
     * @param address
     * @param city
     * @param state
     * @param zip
     * @return
     * @throws Exception
     */
    private static int insertAddress(String address, String city, String state, String zip) throws Exception{
        PreparedStatement insertAddress = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO address(street_name, city, state, zip)" +
                        "VALUES(?, ?, ?, ?)");
        return prepareAddress(insertAddress, address, city, state, zip).executeUpdate();
    }

    /**
     * Returns address id given the address.
     *
     * Used in Patient.insertNewPatient()
     * Used in Patient.updatePatientInfo()
     * Used in Patient.updateAddress()
     *
     * @param address
     * @param city
     * @param state
     * @param zip
     * @return
     * @throws Exception
     */
    private static ResultSet queryAddress(String address, String city, String state, String zip) throws Exception{
        PreparedStatement addressQuery = databaseConnector.getConnection().prepareStatement(
                "SELECT address_id FROM address " +
                        "WHERE street_name = ? AND city = ? AND state = ? AND zip = ?");
        return prepareAddress(addressQuery, address, city, state, zip).executeQuery();
    }

    /**
     * returns all records from address table where the address id given matches the record's address id
     *
     * Used in PatientListController.sendPatientToView()
     * @param addressID
     * @return
     * @throws Exception
     */
    public static ResultSet queryAddress(int addressID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT * FROM `address` WHERE `address_id` = " + addressID
        ).executeQuery();
    }

    /**
     * Given an employee ID, gets all patients referred by that ID.
     * Used in PatientListController.getPatientList()
     * @param employeeID
     * @return
     * @throws Exception
     */
    public static ResultSet queryPatients(int employeeID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
        "SELECT DISTINCT * FROM `refer` " +
                "INNER JOIN patient on refer.patient_id=patient.patient_id " +
                "WHERE refer.employee_id = " + employeeID
        ) .executeQuery();
    }

    /**
     * Updates patient info.
     *
     * Used in PatientViewController.editPatientInfo()
     *
     * @param patientToInsert
     * @param address
     * @param city
     * @param state
     * @param zip
     * @throws Exception
     */
    public static void updatePatientInfo(Patient patientToInsert, String address, String city, String state, String zip) throws Exception{
        Connection conn = databaseConnector.getConnection();
        if (updateAddress(address, city, state, zip) == 1) {

            ResultSet addressSet = queryAddress(address, city, state, zip);
            addressSet.next();
            int address_id = addressSet.getInt("address_id");

            PreparedStatement updateUser = conn.prepareStatement(
                    "UPDATE patient " +
                            "SET patient_id=?, first_name=?, last_name=?, date_of_birth=?, " +
                            "home_phone=?, email=?, insurance_number=?, policy_number=?, address_id=? " +
                            "WHERE patient_id = " + Main.getPatientFocus().getPatientID()
            );

            updateUser.setInt(1, Main.getPatientFocus().getPatientID());
            updateUser.setString(2, patientToInsert.getFirstname());
            updateUser.setString(3, patientToInsert.getLastname());
            updateUser.setString(4, patientToInsert.getDob().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            updateUser.setString(5, String.valueOf(patientToInsert.getPhoneNumber()));
            updateUser.setString(6, patientToInsert.getEmail());
            updateUser.setString(7, String.valueOf(patientToInsert.getInsuranceNumber()));
            updateUser.setString(8, String.valueOf(patientToInsert.getPolicyNumber()));
            updateUser.setInt(9, address_id);
            updateUser.executeUpdate();
        }
    }

    /**
     * Updates an address from info taken from a patient object.
     *
     * Used in Patient.updatePatientInfo()
     * @param address
     * @param city
     * @param state
     * @param zip
     * @return
     * @throws Exception
     */
    public static int updateAddress(String address, String city, String state, String zip) throws Exception{
        String[] addr = Main.getPatientFocus().getAddress().split(", ");
        ResultSet rs = queryAddress(addr[0], addr[1], addr[2], addr[3]);
        rs.next();
        PreparedStatement insertAddress = databaseConnector.getConnection().prepareStatement(
                "UPDATE address " +
                        "SET street_name=?, city=?, state=?, zip= ? " +
                        "WHERE address.address_id = " + rs.getInt("address_id"));
        return prepareAddress(insertAddress, address, city, state, zip).executeUpdate();
    }

    /**
     * Prepares an address statement? May be sloppy code but was used to avoid rewriting this operation in every address method
     * in the Patient class.
     * Used in Patient.insertAddress()
     * Used in Patient.queryAddress()
     * Used in updateAddresses()
     * @param statement
     * @param address
     * @param city
     * @param state
     * @param zip
     * @return
     * @throws Exception
     */
    private static PreparedStatement prepareAddress(PreparedStatement statement, String address, String city, String state, String zip) throws Exception{
        statement.setString(1, address);
        statement.setString(2, city);
        statement.setString(3, state);
        statement.setInt(4, Integer.parseInt(zip));
        return statement;
    }


    ///////////////////
    //Getters/Setters//
    ///////////////////
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
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

    public void setPatientID(int patientID) { this.patientID = patientID; }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getInsuranceNumber(){return insuranceNumber;}
    public void setInsuranceNumber(String insuranceNumber){this.insuranceNumber = insuranceNumber;}

    public String getPolicyNumber() {
        return this.policyNumber;
    }
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public ArrayList<Appointment> getAppointmentList() {
        return AppointmentList;
    }
    public void setAppointmentList(ArrayList<Appointment> appointmentList) {
        AppointmentList = appointmentList;
    }

    public String getPatientFullName() {
        return fullName;
    }
    public void setPatientFullName(String patientFullName) {
        this.fullName = patientFullName;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPatientStatus() { return patientStatus; }
    public void setPatientStatus(String patientStatus) { this.patientStatus = patientStatus; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }





    ////////////////
    //Constructors//
    ////////////////

    /**
     * used in a static call in Main
     * Used in AppointmentListController.setAddAppointment()
     */
    public Patient(){
        this.patientID          = -1;
        this.firstname          = null;
        this.lastname           = null;
        this.dob                = null;
        this.sex                = null;
        this.phoneNumber        = null;
        this.email              = null;
        this.address            = null;
        this.insuranceNumber    = null;
        this.policyNumber       = null;
        this.AppointmentList    = new ArrayList<>();
    }

    /**
     * This constructor is never used. It has stuck around from a previous version of our software where we were doing
     * billing on a per-patient basis rather than a per-appointment basis.
     *
     * @param patientID
     * @param fullName
     * @param billingAddress
     * @param status
     * @param balance
     */
    public Patient(int patientID, String fullName, String billingAddress, String status, float balance){
        this.patientID = patientID;
        this.fullName = fullName;
        this.address = billingAddress;
        this.patientStatus = status;
        this.balance = balance;
    }

    /**
     * This is used in ReferralListController.generatRefferalsList()
     * @param patientID
     * @param firstname
     * @param lastname
     * @param phoneNumber
     */
    public Patient(int patientID, String firstname, String lastname, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.patientID = patientID;
    }

    /**
     * This is used in PatientListController.getPatientList()
     * Used in PatientListController.getPatientList(int)
     * Used in PatientListController.sendPatientToView(Patient)
     * @param PatientID
     * @param firstname
     * @param lastname
     * @param DoB
     * @param Sex
     * @param phoneNumber
     * @param email
     * @param insuranceNumber
     * @param policyNumber
     * @param address
     */
    public Patient(int PatientID, String firstname, String lastname, LocalDate DoB, String Sex, String phoneNumber, String email, String insuranceNumber, String policyNumber, String address ){
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

    /**
     * This is used in NewPatientController.submitToDB()
     * Used in ReferralFormController.submitNewReferral()
     * @param firstname
     * @param lastname
     * @param sex
     * @param email
     * @param dob
     * @param phoneNumber
     * @param insuranceNumber
     * @param policyNumber
     */
    public Patient(String firstname, String lastname, String sex, String email, LocalDate dob, String phoneNumber, String insuranceNumber, String policyNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.insuranceNumber = insuranceNumber;
        this.policyNumber = policyNumber;
    }

    /**
     * Used in PatientViewController.editPatientInfo(ActionEvent)
     * @param firstname
     * @param lastname
     * @param email
     * @param phoneNumber
     * @param insuranceNumber
     * @param policyNumber
     * @param dob
     */
    public Patient(String firstname, String lastname, String email, String phoneNumber, String insuranceNumber, String policyNumber, LocalDate dob) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.insuranceNumber = insuranceNumber;
        this.policyNumber = policyNumber;
        this.dob = dob;
    }
}
