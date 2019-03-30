package Model;

import Controller.databaseConnector;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report {
    String patient_id;
    String name;
    String DoB;
    String Sex;
    String refering_physician;
    String reason;
    int Appointment_id, procedure_id, completed_date, completed_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }


    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
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
    public static ResultSet gatherPatientInfo(String appointment_id, String image_id) throws SQLException {
        System.out.println(
                "SELECT p.patient_id, CONCAT(p.first_name,\" \", p.last_name) AS name, p.date_of_birth, p.sex, CONCAT(e.first_name,\" \", e.last_name) AS refname, ap.reason_for_referral, ap.special_comments, ap.appointment_id, pr.procedure_name, i.imagedata\n" +
                        "FROM patient as p, employees as e, image as i, refer as re, appointments as ap, procedures as pr, procedure_relationship as prr\n" +
                        "WHERE pr.procedure_id = prr.procedure_id AND re.employee_id = e.employee_id AND i.employee_id = e.employee_id AND p.patient_id = re.patient_id AND i.patient_id = p.patient_id AND ap.appointment_id = " + appointment_id + " AND i.image_id = "+ image_id);
        return(databaseConnector.getConnection().prepareStatement(
                "SELECT p.patient_id, CONCAT(p.first_name,\" \", p.last_name) AS name, p.date_of_birth, p.sex, CONCAT(e.first_name,\" \", e.last_name) AS refname, ap.reason_for_referral, ap.special_comments, ap.appointment_id, pr.procedure_name, i.imagedata\n" +
                        "FROM patient as p, employees as e, image as i, refer as re, appointments as ap, procedures as pr, procedure_relationship as prr\n" +
                        "WHERE pr.procedure_id = prr.procedure_id AND re.employee_id = e.employee_id AND i.employee_id = e.employee_id AND p.patient_id = re.patient_id AND i.patient_id = p.patient_id AND ap.appointment_id = " + appointment_id + " AND i.image_id = "+ image_id).executeQuery());

    }


    public Report(String patient_id, String name, String DoB, String Sex, String refering_physician, String reason, int appointment_id){
        this.patient_id = patient_id;
        this.name = name;
        this.DoB = DoB;
        this.Sex = Sex;
        this.refering_physician = refering_physician;
        this.reason = reason;
        this.Appointment_id = appointment_id;

    }
}
/*      rs.getString("name");
        rs.getString("date_of_birth");
        setPatient_id(patient_id);
        rs.getString("sex");
        rs.getString("employee_id");
        rs.getString("reason_for_referral");
        rs.getInt("appointment_id");
        */