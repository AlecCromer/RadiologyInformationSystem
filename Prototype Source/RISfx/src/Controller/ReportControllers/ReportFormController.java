package Controller.ReportControllers;

import Controller.Main;
import Model.Item;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.ResourceBundle;
import java.lang.*;

public class ReportFormController implements Initializable {
    @FXML
    private TextField NameField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField idField;
    @FXML
    private TextField sexField;
    @FXML
    private TextField apptIDField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField physicianField;
    @FXML
    private TextArea reasonField;
    @FXML
    private DatePicker prevExamField;
    @FXML
    private TextArea historyField;
    @FXML
    private TextField timeField;
    @FXML
    private TextArea techniqueField;
    @FXML
    private TextArea findingField;
    @FXML
    private TextField radiologistField;
    @FXML
    private DatePicker signDateField;
    @FXML
    private ComboBox procedureBox;
    @FXML
    private ImageView report_image;

    public static String getPatient_id() {
        return patient_id;
    }

    public static void setPatient_id(String patient_id) {
        ReportFormController.patient_id = patient_id;
    }

    static String patient_id;

    public String getImage_id() {
        return image_id;
    }

    public static void setImage_id(String image_id) {
        ReportFormController.image_id = image_id;
    }

    static String image_id;

    public static void setView(String appointment_id, String image_id) throws Exception{
        System.out.println(appointment_id);
        setPatient_id(patient_id);
        setImage_id(image_id);
        Main.setPopupWindow("ReportViews/ReportForm.fxml");

    }

    public void initialize(URL url, ResourceBundle arg1) {

        try{

            ResultSet rs = Report.gatherPatientInfo(String.valueOf(Main.getAppointmentFocus().getAppointmentId()), getImage_id());

            ObservableList<Report>/*<String>*/ report = FXCollections.observableArrayList();

            while (rs.next()){



                NameField.setText(rs.getString("name"));

                dobField.setText(rs.getString("date_of_birth"));

                idField.setText(rs.getString("patient_id"));
                sexField.setText(rs.getString("sex"));
                apptIDField.setText(rs.getString("appointment_id"));
                dateField.setText(rs.getString("appointment_date"));


                InputStream is = rs.getBinaryStream("imagedata");
                Image image = SwingFXUtils.toFXImage(ImageIO.read(is), null);
                report_image.setImage(image);

                physicianField.setText(rs.getString("refname"));
                reasonField.setText(rs.getString("reason_for_referral"));

            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }



}
