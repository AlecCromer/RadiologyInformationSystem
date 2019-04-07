package Model;

import Controller.databaseConnector;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Report {

    private String firstname;
    private String lastname;
    private String sex;
    private String email;
    private String address;
    private String phoneNumber;
    private String fullName;
    private String refering_physician;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    private String image_id;



    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    private String patient_id;
    private String reason;
    private int Appointment_id, procedure_id, completed_date, completed_time;
    private LocalDate dob;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }




    public String getRefering_physician() {
        return refering_physician;
    }

    public void setRefering_physician(String refering_physician) {
        this.refering_physician = refering_physician;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAppointment_id() {
        return Appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        Appointment_id = appointment_id;
    }

    public int getProcedure_id() {
        return procedure_id;
    }

    public void setProcedure_id(int procedure_id) {
        this.procedure_id = procedure_id;
    }

    public int getCompleted_date() {
        return completed_date;
    }

    public void setCompleted_date(int completed_date) {
        this.completed_date = completed_date;
    }

    public int getCompleted_time() {
        return completed_time;
    }

    public void setCompleted_time(int completed_time) {
        this.completed_time = completed_time;
    }



    //TODO: GENERATE EVERY DETAIL NEEDED FOR A PATIENT'S REVIEW
    public static ResultSet gatherPatientInfo(int appointment_id, String image_id) throws SQLException {
        System.out.println("SELECT DISTINCT p.patient_id, CONCAT(p.first_name,\" \", p.last_name) AS name, p.date_of_birth, ap.appointment_date, p.sex, ap.reason_for_referral, ap.special_comments, ap.appointment_id, pr.procedure_name, i.imagedata, ap.patient_sign_in_time \n" +
                "FROM patient as p, employees as e, image as i, appointments as ap, procedures as pr, procedure_relationship as prr\n" +
                "WHERE pr.procedure_id = prr.procedure_id AND i.patient_id = p.patient_id AND ap.appointment_id = "+ appointment_id + " AND i.image_id = "+ image_id);
        return(databaseConnector.getConnection().prepareStatement(
                "SELECT DISTINCT p.patient_id, CONCAT(p.first_name,\" \", p.last_name) AS name, p.date_of_birth, ap.appointment_date, p.sex, ap.reason_for_referral, ap.special_comments, ap.appointment_id, pr.procedure_name, i.imagedata, ap.patient_sign_in_time \n" +
                        "FROM patient as p, employees as e, image as i, appointments as ap, procedures as pr, procedure_relationship as prr\n" +
                        "WHERE pr.procedure_id = prr.procedure_id AND i.patient_id = p.patient_id AND ap.appointment_id = "+ appointment_id + " AND i.image_id = "+ image_id ).executeQuery());

    }

    public static ResultSet gatherReferralInfo( String image_id) throws SQLException {
            System.out.println("SELECT DISTINCT CONCAT(e.first_name,\" \", e.last_name) AS name " +
                    "FROM employees as e, refer as re, patient as p, image as i " +
                    "WHERE e.employee_id = re.employee_id AND p.patient_id = re.patient_id AND i.patient_id = p.patient_id AND i.image_id ="+ image_id + " LIMIT 1");
        return(databaseConnector.getConnection().prepareStatement("SELECT DISTINCT CONCAT(e.first_name,\" \", e.last_name) AS name " +
                "FROM employees as e, refer as re, patient as p, image as i " +
                "WHERE e.employee_id = re.employee_id AND p.patient_id = re.patient_id AND i.patient_id = p.patient_id AND i.image_id = "+ image_id + " LIMIT 1").executeQuery());
    }

    public static ResultSet queryReports() throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT DISTINCT p.*, i.image_id, a.appointment_id FROM patient as p, image as i, appointments as a WHERE p.patient_id = i.patient_id  AND i.exam_date = a.appointment_date AND a.patient_id = p.patient_id AND i.status  = 'Needs Review' GROUP BY i.image_id;").executeQuery();
    }

    public Report(String patient_id, String name, LocalDate dob, String Sex, String refering_physician, String reason, int appointment_id){
        this.patient_id = patient_id;
        this.fullName = name;
        this.dob = dob;
        this.sex = Sex;
        this.refering_physician = refering_physician;
        this.reason = reason;
        this.Appointment_id = appointment_id;

    }

    public Report(String patient_id, String firstname, String lastname, LocalDate date_of_birth, String sex, String image_id, int appointment_id) {
        this.patient_id = patient_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.dob = date_of_birth;
        this.image_id = image_id;
        this.Appointment_id = appointment_id;

    }

    public static void sendReport(String clinicalIndication, String findings, String image_id, String exam, String employee_name) throws SQLException{
        String[] split = employee_name.trim().split("\\s+");
        System.out.println("SELECT employee_id FROM employees WHERE employees.first_name = '" + split[0]+ "' AND employees.last_name = '"+ split[1]+"'");
        ResultSet rs = databaseConnector.getConnection().prepareStatement("SELECT employee_id FROM employees WHERE employees.first_name = '" + split[0]+ "' AND employees.last_name = '"+ split[1]+"'").executeQuery();
        rs.next();
        int employee_id = rs.getInt("employee_id");
        
        Connection conn = databaseConnector.getConnection();

        PreparedStatement insertNewReport = conn.prepareStatement(
                "INSERT INTO report(report_id, clinical_indication, exam, report_details, employee_id )" +
                        "VALUES (null,?,?,?,?)"
        );


        insertNewReport.setString(1, clinicalIndication);
        insertNewReport.setString(2, exam);
        insertNewReport.setString(3, findings);
        insertNewReport.setInt(4,employee_id );


        insertNewReport.executeUpdate();

        PreparedStatement insertNewReportRelationship = conn.prepareStatement(
                "INSERT INTO image_report_relationship(report_relationship_id, image_id, report_id)" +
                        "VALUES (null,?,?)"
        );
        PreparedStatement getReportId = conn.prepareStatement("SELECT report.report_id FROM report WHERE report.clinical_indication = ? AND report.exam = ? AND report.report_details = ? AND employee_id = ?");
        getReportId.setString(1, clinicalIndication);
        getReportId.setString(2, exam);
        getReportId.setString(3, findings);
        getReportId.setInt(4,employee_id );

        ResultSet rs2 =getReportId.executeQuery();
        rs2.next();

        insertNewReportRelationship.setString(1, image_id);
        insertNewReportRelationship.setString(2, rs2.getString("report_id"));
        insertNewReportRelationship.executeUpdate();

        PreparedStatement updateImageStatus = conn.prepareStatement(
                "UPDATE image SET image.status = 'Complete' WHERE image_id = '"+image_id+"';"
        );
        updateImageStatus.executeUpdate();

    }
}
