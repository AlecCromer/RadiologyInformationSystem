package Model;

import Controller.databaseConnector;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

public class Images {

    private String image_id, machine_id, patient_id, exam_date, employee_id;
    private Image imagedata;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getExam_date() {
        return exam_date;
    }

    public void setExam_date(String exam_date) {
        this.exam_date = exam_date;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public Image getImagedata() {
        return imagedata;
    }

    public void setImagedata(Image imagedata) {
        this.imagedata = imagedata;
    }




    ////////////////////
    //Database Queries//
    ////////////////////
    public static ResultSet queryImageList(String appointmentID)throws Exception{
        return (databaseConnector.getConnection().prepareStatement("SELECT DISTINCT im.imagedata, im.exam_date FROM image as im, appointments as ap WHERE ap.patient_id = im.patient_id and ap.appointment_id and ap.appointment_id = '"+appointmentID+"';")).executeQuery();
    }

    public Images(Image imagedata, String exam_date) {
        this.exam_date = exam_date;
        this.imagedata = imagedata;
    }

    public static boolean insertNewImage(String filePath, int machine_id, String appointment_id) throws SQLException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(filePath));
        Connection conn = databaseConnector.getConnection();
            PreparedStatement insertNewImage = conn.prepareStatement(
                    "INSERT INTO image (image_id, employee_id, exam_date, imagedata, machine_id, patient_id) " +
                            "SELECT null, ap.employee_id, ap.appointment_date, ?, ?, p.patient_id " +
                            "FROM   appointments as ap, patient as p " +
                            "WHERE  ap.patient_id = p.patient_id AND ap.appointment_id = ?;"

            );

        insertNewImage.setBlob(1, inputStream);
        insertNewImage.setInt(2, machine_id);
        insertNewImage.setString(3, appointment_id);

        insertNewImage.executeUpdate();

            return true;
    }



}
