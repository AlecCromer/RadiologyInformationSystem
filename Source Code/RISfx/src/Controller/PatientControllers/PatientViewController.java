package Controller.PatientControllers;

import Controller.AppointmentControllers.AddAppointmentController;
import Controller.AppointmentControllers.AppointmentViewController;
import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PatientViewController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField                     fNameField,    lNameField,      pNumberField,
                                        addressField,  dobField,        sNumberField,
                                        emailField,    InsuranceField,  balanceField, policyField;
    @FXML Button                        EditPatientInfoButton;
    @FXML Label                         reflabel;
    @FXML TableView<Appointment>        patientAppointments;
    @FXML
    TableColumn<Appointment, Time>      patientSignInTime, patientSignOutTime;
    @FXML
    TableColumn<Appointment, Integer>   appointmentID;
    @FXML
    TableColumn<Appointment, String>    patientStatus, appointmentDate;
    private boolean EditPatientLock = false;


      ////////////////
     //Initializers//
    ////////////////

    /**
     * Sets the view to the patient view
     * @throws Exception
     */
    public static void setView()throws Exception{
        Main.setCenterPane("PatientViews/PatientView.fxml");
    }

    /**
     * Initialize the patient view
     * @param url
     * @param arg1
     */
    public void initialize(URL url, ResourceBundle arg1){
        fNameField.setText(Main.getPatientFocus().getFirstname());
        lNameField.setText(Main.getPatientFocus().getLastname());
        pNumberField.setText(String.valueOf(Main.getPatientFocus().getPhoneNumber()));
        addressField.setText(Main.getPatientFocus().getAddress());
        dobField.setText(dateFormatter(Main.getPatientFocus().getDob()));
        emailField.setText(Main.getPatientFocus().getEmail());
        InsuranceField.setText(String.valueOf(Main.getPatientFocus().getInsuranceNumber()));
        policyField.setText(String.valueOf(Main.getPatientFocus().getPolicyNumber()));
        System.out.println(reflabel.getText());
        try{
            ResultSet rs = Patient.queryReferralInfo(Main.getPatientFocus().getPatientID());
            if(rs.next()){

                reflabel.setText("Referring Physician: "+rs.getString("first_name")+ " " + rs.getString("last_name"));
            }else{
                reflabel.setText("No referring physician");
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        updateAppointmentTable();
        patientAppointments.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendAppointmentToView(patientAppointments.getSelectionModel().getSelectedItem());
                    AppointmentViewController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Updates the appointment table
     */
    private void updateAppointmentTable(){
        try {
            patientAppointments.setItems(generatePatientAppointmentList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        appointmentDate.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
        patientSignInTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("patientSignIn"));
        patientSignOutTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("patientSignOut"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
    }

      ////////////////////
     //Database Queries//
    ////////////////////




      ///////////////////
     //List Generators//
    ///////////////////

    /**
     * Creates the appointment list
     * @return
     * @throws Exception
     */
    private ObservableList<Appointment> generatePatientAppointmentList() throws Exception{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet rs = Patient.queryPatientAppointments(Main.getPatientFocus().getPatientID())){
            while (rs.next()){
                appointments.add(generateAppointment(rs));
            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return appointments;
    }

    /**
     * Generates the appointment date
     * @param rs
     * @return
     * @throws Exception
     */
    private Appointment generateAppointment(ResultSet rs) throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String date = format.format(rs.getDate("appointment_date"));
        return new Appointment(
                rs.getInt("appointment_id"),
                date,
                rs.getTime("patient_sign_in_time"),
                rs.getTime("patient_sign_out_time"),
                rs.getString("patient_status")
        );
    }


      //////////////////
     //Button Methods//
    //////////////////

    /**
     * Allows the patient info to be changed
     * @param actionEvent
     * @throws Exception
     */
    public void editPatientInfo(ActionEvent actionEvent) throws Exception {
        if (!EditPatientLock) {
            EditPatientLock = true;
            fNameField.setDisable(false);
            lNameField.setDisable(false);
            pNumberField.setDisable(false);
            addressField.setDisable(false);
            dobField.setDisable(false);
            InsuranceField.setDisable(false);
            policyField.setDisable(false);
            emailField.setDisable(false);
            EditPatientInfoButton.setText("Submit");
        } else {
            EditPatientLock = false;
            fNameField.setDisable(true);
            lNameField.setDisable(true);
            pNumberField.setDisable(true);
            addressField.setDisable(true);
            dobField.setDisable(true);
            emailField.setDisable(true);
            InsuranceField.setDisable(true);
            policyField.setDisable(true);
            EditPatientInfoButton.setText("Edit Patient Info.");

            String[] addr = addressField.getText().split(", ");

            //Patient patientToInsert, String address, String city, String state, String zip
            Patient.updatePatientInfo(new Patient(
                    //String firstname, String lastname, String email, String phoneNumber, String insuranceNumber, String policyNumber, LocalDate dob
                    fNameField.getText(),
                    lNameField.getText(),
                    emailField.getText(),
                    pNumberField.getText(),
                    InsuranceField.getText(),
                    policyField.getText(),
                    LocalDate.parse(dobField.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            ),
                addr[0],
                addr[1],
                addr[2],
                addr[3]);
        }
    }

    public void setAppointmentView(ActionEvent actionEvent) throws Exception {
        AppointmentViewController.setView();
    }

    public void setAddAppointment(ActionEvent actionEvent) throws Exception {
        AddAppointmentController.setView();
    }

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }


      ///////////////////
     //Form Validation//
    ///////////////////

    /**
     * Formats the date correctly
     * @param date
     * @return
     */
    private String dateFormatter(LocalDate date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(format);
    }

    /**
     * Sends appointment information to the focus
     * @param selectedItem
     * @throws Exception
     */
    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();
        ResultSet rs = Appointment.queryAppointmentFocus(appointmentId);
        rs.next();
        Main.setAppointmentFocus(Appointment.generateAppointmentFocus(rs));
    }

    ///////////////////
    //Form Validation//
    ///////////////////

    private void checkField() {
        if (!fNameField.getText().matches("^(?=.*[a-zA-Z]).*$")) {
            error(0);
        } else {
            error(1);
        }
        if (!lNameField.getText().matches("^(?=.*[a-zA-Z]).*$")) {
            error(2);
        } else {
            error(3);
        }
        if (!pNumberField.getText().matches("^(?=.*[0-9])[a-zA-Z\\d\\s\\-#.+]+.*$")) {
            error(4);
        } else {
            error(5);
        }
        if (!addressField.getText().matches("^(?=.*[a-zA-Z]).*$")) {
            error(6);
        } else {
            error(7);
        }
        if (!dobField.getText().matches("^(?=.*[a-z])+(?=.*[A-Z]).*$")) {
            error(8);
        } else {
            error(9);
        }
        if (!sNumberField.getText().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")) { //Come back to this
            error(10);
        } else {
            error(11);
        }
        if (!emailField.getText().matches("^1?[\\(\\- ]*\\d{3}[\\)-\\. ]*\\d{3}[-\\. ]*\\d{4}$")) { //Was mapping out logic will clean up later
            error(12);
        } else {
            error(13);
        }
        if (!InsuranceField.getText().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            error(14);
        } else {
            error(15);
        }
        if (!balanceField.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")) {
            error(16);
        } else {
            error(17);
        }
        if (!policyField.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")) {
            error(18);
        } else {
            error(19);
        }

    }
    private void error ( int fieldID){

        switch (fieldID) {
            case 0: {
           /* patentFirstName.clear(); not sure if it would be good to clear and provide a hint for them
            patentFirstName.setPromptText("Need to capitalize first letter of name");*/
                fNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 1: {
                fNameField.setStyle(null);
                return;
            }
            case 2: {
            /* patentFirstName.clear();
            patentFirstName.setPromptText("Need to capitalize first letter of name");*/
                lNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 3: {
                lNameField.setStyle(null);
                return;
            }
            case 4: {
                pNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 5: {
                pNumberField.setStyle(null);
                return;
            }
            case 6: {
                addressField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 7: {
                addressField.setStyle(null);
                return;
            }
            case 8: {
                dobField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 9: {
                dobField.setStyle(null);
                return;
            }
            case 10: {
                sNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 11: {
                sNumberField.setStyle(null);
                return;
            }
            case 12: {
                emailField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 13: {
                emailField.setStyle(null);
                return;
            }
            case 14: {
                InsuranceField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 15: {
                InsuranceField.setStyle(null);
                return;
            }
            case 16: {
                balanceField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 17: {
                balanceField.setStyle(null);
                return;
            }
            case 18: {
                policyField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 19: {
                policyField.setStyle(null);
                return;
            }
        }
    }
    }
