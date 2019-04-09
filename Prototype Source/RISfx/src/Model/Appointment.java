package Model;

import Controller.Main;
import Controller.databaseConnector;

import java.sql.*;
import java.text.SimpleDateFormat;

public class Appointment {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    private int appointmentId, procedureId, patientId, machineId, employeeId;
    private Date appointmentDate;
    private Time appointmentTime, patientSignIn, patientSignOut;
    private String referralReason, Comments, patientFullName, patientStatus, dateTime, technician, machineName, procedureName;
    private String[] address;
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
    public static ResultSet queryForBillingAppointments()throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(patient.first_name, \" \", patient.last_name) AS full_name, procedures.procedure_name " +
                        "FROM `appointments` " +
                        "INNER JOIN patient ON appointments.patient_id=patient.patient_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "WHERE appointments.patient_status = 'Signed Out' OR appointments.patient_status = 'Billed'")).executeQuery();
    }
    public static ResultSet queryForBillingAppointments(int patientID) throws Exception{
        return (databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.*, CONCAT(patient.first_name, \" \", patient.last_name) AS full_name, procedures.procedure_name " +
                        "FROM appointments " +
                        "INNER JOIN patient ON appointments.patient_id=patient.patient_id " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id  " +
                        "WHERE (appointments.patient_status = 'Signed Out' OR appointments.patient_status = 'Billed') AND appointments.patient_id = " + patientID)).executeQuery();
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
                        "WHERE appointments.employee_id="+employeeID
                //" AND appointments.patient_status != 'Signed In'"
        )).executeQuery();
    }


    public static ResultSet queryBillingList(int appointmentId) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT billing.*, " +
                        "items.item_name, items.item_cost, SUM(items.item_cost) as 'item_cost_total', COUNT(items.item_cost) AS 'item_amount', " +
                        "appointments.appointment_date, appointments.patient_status, " +
                        "patient.first_name, patient.last_name " +
                        "FROM billing " +
                        "INNER JOIN appointments ON billing.appointment_id=appointments.appointment_id " +
                        "INNER JOIN items ON billing.item_id=items.item_id " +
                        "INNER JOIN patient ON appointments.patient_id=patient.patient_id " +
                        "WHERE appointments.appointment_id = " + appointmentId
        ).executeQuery();
    }

    public static float queryProcedurePrice(int procedureID) throws Exception{
        ResultSet rs = databaseConnector.getConnection().prepareStatement(
                "SELECT procedure_price FROM procedures WHERE procedure_id = " + procedureID
        ).executeQuery();
        rs.next();
        return rs.getFloat("procedure_price");
    }

    public static void submitNewAppointment(Appointment appointmentToSubmit) throws Exception{
        PreparedStatement update = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `appointments` (`patient_id`, `appointment_date`, `appointment_time`, `procedure_id`, `machine_id`, `employee_id`) " +
                        "VALUES (?, ?, ?, ?, ?, ?);");
        update.setInt(1, appointmentToSubmit.getPatientId());
        update.setDate(2, appointmentToSubmit.getAppointmentDate());
        update.setTime(3, appointmentToSubmit.getAppointmentTime());
        update.setInt(4, appointmentToSubmit.getProcedureId());
        update.setInt(5, appointmentToSubmit.getMachineId());
        update.setInt(6, appointmentToSubmit.getEmployeeId());
        update.executeUpdate();
    }

    public static void updateSignInTime() throws Exception{
        PreparedStatement statement = databaseConnector.getConnection().prepareStatement(
                "UPDATE appointments " +
                        "SET appointments.patient_sign_in_time = ?, appointments.patient_status = 1 " +
                        "WHERE appointments.appointment_id = ?");
        statement.setTime(1, Main.getAppointmentFocus().getPatientSignIn());
        statement.setInt(2, Main.getAppointmentFocus().getAppointmentId());
        statement.executeUpdate();
    }
    public static void updateSignOutTime() throws Exception{
        PreparedStatement statement = databaseConnector.getConnection().prepareStatement(
                "UPDATE appointments " +
                        "SET appointments.patient_sign_out_time = ?, appointments.patient_status = 2 " +
                        "WHERE appointments.appointment_id = ?");
        statement.setTime(1, Main.getAppointmentFocus().getPatientSignOut());
        statement.setInt(2, Main.getAppointmentFocus().getAppointmentId());
        statement.executeUpdate();
    }
    public static boolean updateReadyStatus(int appointmentId) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "UPDATE appointments " +
                        "SET appointments.patient_status = 'Needs Report' " +
                        "WHERE appointments.appointment_id = " + appointmentId
        ).execute();
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

    public String getReferralReason() {
        return referralReason;
    }
    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
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
    public void setBalance() throws Exception{
        ResultSet rs = queryBillingList(this.appointmentId);
        rs.next();
        ResultSet results = Procedure.queryProcedureInfo(this.procedureId);
        results.next();
        this.balance = rs.getFloat("item_cost_total") + results.getFloat("procedure_price");
    }

    public void setAddress()throws Exception{
        ResultSet rs =  Patient.queryPatientInfo(this.patientId);
        rs.next();
        rs = Patient.queryAddress(rs.getInt("address_id"));
        rs.next();
        String[] returnArr = {rs.getString("street_name"), rs.getString("city"), rs.getString("zip"), rs.getString("state")};
        this.address = returnArr;
    }
    public String getAddress(){
        return address[0] + " " + address[1] + " " + address[2] + " " + address[3];
    }
    public String[] getAddressAsArray(){return address;}

    ////////////////
    //Constructors//
    ////////////////
    public Appointment(){
        this.appointmentDate = null;
        this.appointmentTime = null;
        this.patientSignIn = null;
        this.patientSignOut = null;
        this.referralReason = null;
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

    public Appointment(int appointmentId, int patientId, int procedureId, String procedureName,
                       String patientFullName, String patientStatus, Date appointmentDate) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.patientFullName = patientFullName;
        this.patientStatus = patientStatus;
        this.appointmentDate = appointmentDate;
    }

    public Appointment(int appointmentId, int procedure, int patientId, String patientFullName, int machineId, int employeeId, Date appointmentDate,
                       Time appointmentTime, Time patientSignIn, Time patientSignOut, String referralReason, String Comments, String patientStatus, String dateTime, String technician, String procedureName){
        this.appointmentDate    = appointmentDate;
        this.appointmentTime    = appointmentTime;
        this.patientSignIn      = patientSignIn;
        this.patientSignOut     = patientSignOut;
        this.referralReason = referralReason;
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

