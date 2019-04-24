package Model;

import Controller.databaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Referral {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private Procedure procedureRequested;
    private Employee referrer;
    private Patient patient;
    private boolean isProcessed;
    private String urgency;
    private String heartRate;
    private String referralReason, comments;
    private int height, weight, heart_rate, systolic_pressure, diastolic_pressure;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getSystolic_pressure() {
        return systolic_pressure;
    }

    public void setSystolic_pressure(int systolic_pressure) {
        this.systolic_pressure = systolic_pressure;
    }

    public int getDiastolic_pressure() {
        return diastolic_pressure;
    }

    public void setDiastolic_pressure(int diastolic_pressure) {
        this.diastolic_pressure = diastolic_pressure;
    }

/////////////////////
     //Object Generators//
    /////////////////////


      ////////////////////
     //Database Queries//
    ////////////////////
    /**
     * Gets unprocessed referrals.
     * If this descriptor isn't updated it means we missed this when checking documentation.
     *
     * @return
     * @throws Exception
     */
    public static ResultSet queryUnprocessedReferrals()throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT patient.patient_id, patient.first_name AS patient_first_name, patient.last_name AS patient_last_name, patient.home_phone, " +
                        "employees.first_name AS referrer_first_name, employees.last_name AS referrer_last_name, employees.email AS referrer_email, " +
                        "procedures.procedure_name, procedures.procedure_length, " +
                        "refer.referring_id, refer.employee_id, refer.procedure_id, refer.is_processed, refer.urgency, refer.height, refer.weight, refer.heart_rate, refer.systolic_pressure, refer.diastolic_pressure " +

                        "FROM `refer` " +

                        "INNER JOIN patient ON patient.patient_id=refer.patient_id " +
                        "INNER JOIN employees ON employees.employee_id=refer.employee_id " +
                        "INNER JOIN procedures ON procedures.procedure_id=refer.procedure_id " +

                        "WHERE is_processed = 0"
        ).executeQuery();
    }

    public static void insertNewReferral(int patientID, int employeeID, int procedureID, String urgency, String referralReason, String specialComments, int height, int weight, int heartRate, int systolicPressure, int diastolicPressure) throws Exception{

        PreparedStatement insertNewReferral = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO refer(employee_id, patient_id, procedure_id, is_processed, urgency, reason_for_referral, special_comments, height, weight, heart_rate, systolic_pressure, diastolic_pressure)" +
                        "VALUES (?, ?, ?, 0, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        insertNewReferral.setInt(1,employeeID);
        insertNewReferral.setInt(2,patientID);
        insertNewReferral.setInt(3,procedureID);
        insertNewReferral.setString(4,urgency);
        insertNewReferral.setString(5,referralReason);
        insertNewReferral.setString(6,specialComments);
        insertNewReferral.setInt(7, height);
        insertNewReferral.setInt(8, weight);
        insertNewReferral.setInt(9, heartRate);
        insertNewReferral.setInt(10, systolicPressure);
        insertNewReferral.setInt(11, diastolicPressure);

        insertNewReferral.executeUpdate();
    }


      ///////////////////
     //Getters/Setters//
    ///////////////////
    public Procedure getProcedureRequested() {
        return procedureRequested;
    }
    public void setProcedureRequested(Procedure procedureRequested) {
        this.procedureRequested = procedureRequested;
    }

    public Employee getReferrer() {
        return referrer;
    }
    public void setReferrer(Employee referrer) {
        this.referrer = referrer;
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean isProcessed() {
        return isProcessed;
    }
    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public String getUrgency() {
        return urgency;
    }
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    //These getters drill down into the objects
    public int getPatientID(){ return patient.getPatientID(); }
    public String getPatientFirstName(){ return patient.getFirstname(); }
    public String getPatientLastName(){ return patient.getLastname(); }
    public String getReferrerFullName(){ return referrer.getFirstName()+ " "+referrer.getLastName();}
    public String getProcedureName(){ return procedureRequested.getProcedureName(); }
    public String getPatientPhone(){
        char[] phone = String.valueOf(patient.getPhoneNumber()).toCharArray();
        return "(" + phone[0]+phone[1]+phone[2]+ ") " + phone[3]+phone[4]+phone[5];
    }


      ////////////////
     //Constructors//
    ////////////////
    /**
     * Used in ReferralListController.generateRefferalsList()
     * @param procedureRequested
     * @param referrer
     * @param patient
     * @param isProcessed
     * @param urgency
     * @param height
     * @param weight
     * @param heart_rate
     * @param systolic_pressure
     * @param diastolic_pressure
     */
    public Referral(Procedure procedureRequested, Employee referrer, Patient patient, boolean isProcessed, String urgency, int height, int weight, int heart_rate, int systolic_pressure, int diastolic_pressure) {
        this.procedureRequested = procedureRequested;
        this.referrer = referrer;
        this.patient = patient;
        this.isProcessed = isProcessed;
        this.urgency = urgency;
        this.height = height;
        this.weight = weight;
        this.heart_rate = heart_rate;
        this.systolic_pressure = systolic_pressure;
        this.diastolic_pressure = diastolic_pressure;
    }

    public Referral(Procedure procedureRequested, Employee referrer, Patient patient, boolean isProcessed, String urgency, String heartRate, String referralReason, String comments) {
        this.procedureRequested = procedureRequested;
        this.referrer = referrer;
        this.patient = patient;
        this.isProcessed = isProcessed;
        this.urgency = urgency;
        this.heartRate = heartRate;
        this.referralReason = referralReason;
        this.comments = comments;

    }
}
