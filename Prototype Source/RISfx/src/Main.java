import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage = new Stage();
    //Use a boarderPane to present Unified Menu across all windows
    //Use methods to edit
    private static BorderPane Outer = new BorderPane();
    private static BorderPane RIS_Container = new BorderPane();
    private static Node backNode;
    private static Scene MainScene = new Scene(Outer,1600, 900);
    private static Stage popup = new Stage();

    @FXML TextField fNameField, lNameField, pNumberField, addressField, dobField, sNumberField, emailField, InsuranceField, balanceField;
    @FXML Button EditPatientInfoButton, patientSubmit;
    @FXML CheckBox toggler;
    @FXML DatePicker scheduleDate;
    @FXML TableView scheduleTime;
    @FXML ComboBox appointmentCBox;

    public boolean EditPatientLock = false;

    @Override
    public void start(Stage stage) throws Exception{
        primaryStage.setTitle("RIS Clinic System");
        primaryStage.setScene(MainScene);
        //Set the TOP of the borderPane to our menu
        Outer.setTop(FXMLLoader.load(getClass().getResource("/View/RIS_Menu.fxml")));
        RIS_Container.setTop(FXMLLoader.load(getClass().getResource("/View/RIS_Tabs.fxml")));
        Outer.setCenter(RIS_Container);


        //Set the initial start to our PatientList
        setPatientList();

        //Finally show initial stage
        primaryStage.show();
    }

    //Called to set the main view
    private void setCenterPane(String fxmlName)throws Exception{
        try {
            RIS_Container.setRight(null);
            backNode = RIS_Container.getCenter();
            RIS_Container.setCenter(FXMLLoader.load(getClass().getResource("/View/"+fxmlName)));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setBackPage(){
        RIS_Container.setRight(null);
        RIS_Container.setCenter(backNode);
    }

    //Individual view setters
    //Call Model Objects within each of these
    public void setPatientView()throws Exception{
        setCenterPane("PatientView.fxml");
    }
    public void setAddPatientView()throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View/addPatient.fxml"));
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.show();
    }
    public void setPatientList()throws Exception{
        setCenterPane("PatientList.fxml");
    }
    public void setAppointmentList()throws Exception{
        setCenterPane("AppointmentList.fxml");
    }
    public void setAppointmentView()throws Exception{
        setCenterPane("AppointmentView.fxml");
    }
    public void setTechApptView() throws Exception{
        setCenterPane("TechEntry.fxml");
    }
    public void setAddAppointment()throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View/addAppointment.fxml"));
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.show();
    }
    public void setReferralView() throws Exception{
        setCenterPane("ReferralList.fxml");
    }
    public void setBillingList() throws Exception{
        setCenterPane("BillingList.fxml");
    }
    public void setBillView() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/sample.fxml"));
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.show();
    }
    public void setWorkList() throws Exception{
        setCenterPane("WorkList.fxml");
    }

    //Button Functions
    public void editPatientInfo(){
        if (!EditPatientLock) {
            EditPatientLock = true;
            fNameField.setDisable(false);
            lNameField.setDisable(false);
            pNumberField.setDisable(false);
            addressField.setDisable(false);
            dobField.setDisable(false);
            sNumberField.setDisable(false);
            InsuranceField.setDisable(false);
            balanceField.setDisable(false);
            emailField.setDisable(false);
            EditPatientInfoButton.setText("Submit");
        }
        else{
            EditPatientLock = false;
            fNameField.setDisable(true);
            lNameField.setDisable(true);
            pNumberField.setDisable(true);
            addressField.setDisable(true);
            dobField.setDisable(true);
            sNumberField.setDisable(true);
            emailField.setDisable(true);
            InsuranceField.setDisable(true);
            balanceField.setDisable(true);
            EditPatientInfoButton.setText("Edit Patient Info.");
        }
    }
    public void toggleSchedule(){
        if (toggler.isSelected()){
            scheduleDate.setVisible(true);
            scheduleTime.setVisible(true);
            appointmentCBox.setVisible(true);
            popup.setHeight(popup.getHeight() + 240);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() + 290);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() + 18);
        }
        else {
            scheduleDate.setVisible(false);
            scheduleTime.setVisible(false);
            appointmentCBox.setVisible(false);
            popup.setHeight(popup.getHeight() - 240);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() - 290);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() - 18);
        }
    }
    public void showFiles() throws Exception{
        RIS_Container.setRight(FXMLLoader.load(getClass().getResource("/View/FilesViewer.fxml")));
    }

    //Useless but necessary
    public static void main(String[] args) {
        launch(args);
    }
}
