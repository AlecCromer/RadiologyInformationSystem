package Controller.PatientControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PatientListController implements Initializable {

      ///////////////////////
     //Fill Table Methods//
    ///////////////////////

    @FXML private TableView<Patient> PatientList;
    @FXML private TableColumn<Patient, String> patientID;
    @FXML private TableColumn<Patient, String> firstname;
    @FXML private TableColumn<Patient, String> lastname;
    @FXML private TableColumn<Patient, String> dob;
    @FXML private TableColumn<Patient, String> sex;
    @FXML private TableColumn<Patient, Integer> pnumber;
    @FXML private TableColumn<Patient, String> email;

    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
        patientListFill();
        PatientList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    setPatientView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void patientListFill() {
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
        pnumber.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("pnumber"));
        email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));
    }

    public ObservableList<Patient>/*<String>*/  getPatientList() throws IOException {
        ObservableList<Patient>/*<String>*/ patients = FXCollections.observableArrayList();

        try(
                Connection conn = databaseConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(
                        "select * " +
                                "FROM patient");
                ResultSet resultSet = displayprofile.executeQuery();

        ){
            while (resultSet.next()){
                patients.add(new Patient(resultSet.getInt("patient_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("date_of_birth"), resultSet.getString("sex"),
                        resultSet.getInt("home_phone"), resultSet.getString("email")));


            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return patients;


    }

      ///////////////////////
     //Change View Methods//
    ///////////////////////
    public void setAddPatientView()throws Exception{
        NewPatientController.setView();
    }

    public static void setPatientView()throws Exception{
       PatientViewController.setView();
    }

    public static void setPatientList()throws Exception{
        Main.setCenterPane("PatientViews/PatientList.fxml");
    }
}
