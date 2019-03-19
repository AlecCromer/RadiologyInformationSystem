package Controller.PatientControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PatientListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////

    @FXML private TableView<Patient>            PatientList;
    @FXML private TableColumn<Patient, String>  patientID, firstname, lastname, dob, sex, email;
    @FXML private TableColumn<Patient, Integer>     phoneNumber;


      ////////////////
     //Initializers//
    ////////////////
    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
        updateTable();
        PatientList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendPatientToView(PatientList.getSelectionModel().getSelectedItem());
                    setPatientView();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setView()throws Exception{
        Main.setCenterPane("PatientViews/PatientList.fxml");
    }

    public void updateTable() {
        try {

            PatientList.setItems(getPatientList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        patientID.setCellValueFactory(new PropertyValueFactory<Patient, String>("patientID"));
        firstname.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastname"));
        dob.setCellValueFactory(new PropertyValueFactory<Patient, String>("dob"));
        sex.setCellValueFactory(new PropertyValueFactory<Patient, String>("sex"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("phoneNumber"));
        email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));
    }


      ////////////////////
     //Database Queries//
    ////////////////////
    private ResultSet queryAllPatients() {
        ResultSet resultSet = null;
        try {
            Connection conn = databaseConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement(
                    "select * FROM patient");
             resultSet = displayprofile.executeQuery();
        }
        catch(SQLException ex) {
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
        }
        return resultSet;
    }


      ///////////////////
     //List Generators//
    ///////////////////
    public ObservableList<Patient>  getPatientList() throws Exception {
        ObservableList<Patient> patients = FXCollections.observableArrayList();

        ResultSet resultSet = queryAllPatients();
        while (resultSet.next()) {
            patients.add(new Patient(
                    resultSet.getInt("patient_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    dateFormatter(resultSet.getString("date_of_birth")),
                    resultSet.getString("sex"),
                    resultSet.getInt("home_phone"),
                    resultSet.getString("email"),
                    resultSet.getInt("insurance_number"),
                    resultSet.getInt("policy_number"),
                    ""
            ));
        }
        return patients;
    }


      //////////////////
     //Button Methods//
    //////////////////
    public void setAddPatientView()throws Exception{
        NewPatientController.setView();
    }

    public static void setPatientView()throws Exception{
       PatientViewController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private LocalDate dateFormatter(String date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, format);
    }

    private void sendPatientToView(Patient selectedItem) throws Exception{
        int patient_id = selectedItem.getPatientID();

        Connection conn = databaseConnector.getConnection();
        PreparedStatement selectPatient = conn.prepareStatement(
                "select * FROM patient WHERE `patient_id` = ?");
        selectPatient.setInt(1, patient_id);

        PreparedStatement addressFill = conn.prepareStatement(
                "SELECT * FROM `address` WHERE `address_id` = ?");

        ResultSet rs = selectPatient.executeQuery();
        rs.next();

        addressFill.setInt(1, rs.getInt("address_id"));
        ResultSet addr = addressFill.executeQuery();
        addr.next();
        String address = addr.getString("street_name") + ", " + addr.getString("city") + ", " + addr.getString("state") + ", " + addr.getInt("zip") ;
        Main.setPatientFocus((new Patient(
                rs.getInt("patient_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                LocalDate.parse(rs.getString("date_of_birth")),
                rs.getString("sex"),
                rs.getInt("home_phone"),
                rs.getString("email"),
                rs.getInt("insurance_number"),
                rs.getInt("policy_number"),
                address
                )));
    }
}
