package Model;

import Controller.databaseConnector;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

public class Images {

    private String image_id;
    private String machine_id;
    private String patient_id;
    private String exam_date;
    private String employee_id;
    private String status;
    private Image imagedata;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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


    /**
     * Returns a list of the images from a specified appointment
     * @param appointmentID the appointment id of the selected image
     * @return ResultSet of the SQL query
     * @throws Exception if the SQL returns an error
     */
    public static ResultSet queryImageList(String appointmentID)throws Exception{
        return (databaseConnector.getConnection().prepareStatement("SELECT DISTINCT im.imagedata, im.exam_date, im.status, im.image_id FROM image as im, appointments as ap WHERE ap.patient_id = im.patient_id and ap.appointment_date = im.exam_date and ap.appointment_id = '"+appointmentID+"';")).executeQuery();
    }

    /**
     * constructor to initialize the images
     * @param imagedata
     * @param exam_date
     * @param status
     * @param image_id
     */
    public Images(Image imagedata, String exam_date, String status, String image_id) {
        this.exam_date = exam_date;
        this.imagedata = imagedata;
        this.status = status;
        this.image_id = image_id;
    }

    /**
     * Inserts an image into the database
     * @param filePath the file path of the image
     * @param machine_id the machine id of the machine used to take the image
     * @param appointment_id the appointment id of the appointment that the image was taken during
     * @return true if the insert was successful
     * @throws SQLException if the SQL returns an error
     * @throws FileNotFoundException if there is no image with the selected filepath
     */
    public static boolean insertNewImage(String filePath, int machine_id, String appointment_id) throws SQLException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(filePath));
        Connection conn = databaseConnector.getConnection();
        PreparedStatement insertNewImage = conn.prepareStatement(
                "INSERT INTO image (image_id, employee_id, exam_date, imagedata, machine_id, patient_id, status) " +
                        "SELECT null, ap.employee_id, ap.appointment_date, ?, ?, p.patient_id, 'Needs Review' " +
                        "FROM appointments as ap, patient as p " +
                        "WHERE ap.patient_id = p.patient_id AND ap.appointment_id = ?;"

        );

        insertNewImage.setBlob(1, inputStream);
        insertNewImage.setInt(2, machine_id);
        insertNewImage.setString(3, appointment_id);
        insertNewImage.executeUpdate();

        return true;
    }

}
