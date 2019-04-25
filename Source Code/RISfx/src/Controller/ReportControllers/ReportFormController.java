package Controller.ReportControllers;

import Controller.Main;
import Model.Report;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.lang.*;

/**
 * ReportFormController is used to create a report for a scanned image
 */
public class ReportFormController implements Initializable {
    @FXML
    private TextField   NameField, dobField, idField, sexField,
                        apptIDField, dateField, physicianField,
                        timeField, radiologistField, clinicalIndication, procedureRequested;
    @FXML
    private TextArea reasonField, historyField, techniqueField, findingField;
    @FXML
    private DatePicker prevExamField, signDateField;
    @FXML
    private ImageView report_image;
    @FXML
    private Button submit_report;

    static String patient_id;
    static String image_id;
    static int appointment_id;

    public static boolean isComplete() {
        return complete;
    }

    public static void setComplete(boolean complete) {
        ReportFormController.complete = complete;
    }

    static boolean complete;

    public static String getPatient_id() {
        return patient_id;
    }

    public static void setPatient_id(String patient_id) {
        ReportFormController.patient_id = patient_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public static void setImage_id(String image_id) {
        ReportFormController.image_id = image_id;
    }

    public static int getAppointment_id() {
        return appointment_id;
    }

    public static void setAppointment_id(int appointment_id) {
        ReportFormController.appointment_id = appointment_id;
    }

    /**
     * Method sets the view
     * @param appointment_id The appointment id of the patient's report
     * @param image_id The image id that the radiologist selected
     * @param complete if the report being viewed is complete
     * @throws Exception throws exception if setting view returns an error
     */
    public static void setView(int appointment_id, String image_id, boolean complete) throws Exception{
        setComplete(complete);
        setAppointment_id(appointment_id);
        setPatient_id(patient_id);
        setImage_id(image_id);
        Main.setPopupWindow("ReportViews/ReportForm.fxml");
        Main.getPopup().setWidth(625);
        Main.getPopup().setHeight(700);
    }

    /**
     * Initializes the report form by populating the fields
     * @param url the location for relative paths
     * @param arg1 the resources used to localize the root object
     */
    public void initialize(URL url, ResourceBundle arg1){
        historyField.setDisable(true);
        reasonField.setDisable(true);
        if(isComplete()){
            try{
                ResultSet reportDetails = Report.getReportDetails(image_id);
                reportDetails.next();
                clinicalIndication.setText(reportDetails.getString("clinical_indication"));
                findingField.setText(reportDetails.getString("report_details"));
                radiologistField.setText(reportDetails.getString("name"));
                submit_report.setVisible(false);
                clinicalIndication.setDisable(true);
                findingField.setDisable(true);
                reasonField.setDisable(true);
                historyField.setDisable(true);
                radiologistField.setDisable(true);
                signDateField.setVisible(false);
            }catch(Exception e){

            }

        }
        ArrayList pms = Main.getSessionUser().getPermissions();
        if(pms.contains(1)){
            submit_report.setVisible(false);

        }
        try{
            ResultSet rs = Report.gatherPatientInfo(getAppointment_id(), getImage_id());

            while (rs.next()){
                NameField.setText(rs.getString("name"));
                dobField.setText(rs.getString("date_of_birth"));
                idField.setText(rs.getString("patient_id"));
                sexField.setText(rs.getString("sex"));
                apptIDField.setText(rs.getString("appointment_id"));
                dateField.setText(rs.getString("appointment_date"));
                timeField.setText(rs.getString("patient_sign_in_time"));
                reasonField.setText(rs.getString("reason_for_referral"));
                historyField.setText(rs.getString("special_comments"));
                procedureRequested.setText(rs.getString("procedure_name"));

                InputStream is = rs.getBinaryStream("imagedata");

                Image image = SwingFXUtils.toFXImage(ImageIO.read(is), null);
                report_image.setImage(image);


            }
            rs.close();
            ResultSet rs2 = Report.gatherReferralInfo(getImage_id());
            if(rs2.next()){
                physicianField.setText(rs2.getString("name"));
            }else{
                physicianField.setText("No referring physician");
            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }

    /**
     * Button submission when the report is complete
     * Sends the submitted data to create the report in the database
     * If fails, no report is made
     * @throws SQLException if the SQL has an error
     */
    public void submitReport() throws SQLException{
        if(!findingField.getText().isEmpty() || !clinicalIndication.getText().isEmpty() || checkRadiologist(NameField.getText())){
            Report.sendReport(clinicalIndication.getText(), findingField.getText(), getImage_id(), procedureRequested.getText(), radiologistField.getText());
            Main.getOuter().setEffect(null);
            Main.popup.close();
        }
    }

    /**
     * Checks if the signature for the radiologist matches the person logged in
     * @param radiologist The submitted text for the radiologist field
     * @return boolean true if logged in user matches the signature
     */
    public boolean checkRadiologist(String radiologist){
        if(Main.getSessionUser().getPermissions().contains(2) || Main.getSessionUser().getPermissions().contains(6)){
            if(radiologist == Main.getSessionUser().getFullName()){
                return true;
            }
            return false;
        }
        return false;
    }
}
