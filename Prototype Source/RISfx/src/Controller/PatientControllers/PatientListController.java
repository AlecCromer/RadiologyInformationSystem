package Controller.PatientControllers;

import Controller.Main;
import Controller.ReferralControllers.ReferralFormController;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientListController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////

    @FXML
    private TableView<Patient> PatientList;
    @FXML
    private TableColumn<Patient, String> patientID, firstname, lastname, dob, sex, email;
    @FXML
    private TableColumn<Patient, Integer> phoneNumber;
    @FXML
    private TextField searchField;

    private  ArrayList pms;


    ////////////////
    //Initializers//
    ////////////////
    /**
     * This method is used to add two integers. This is
     * a the simplest form of a class method, just to
     * show the usage of various javadoc Tags.
     * @param url This is the first paramter to addNum method
     * @param arg1 This is the second parameter to addNum method
     * @return void This returns sum of url and arg1.
     */
    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
        pms = Main.getSessionUser().getPermissions();
        if(pms.contains(1)){
            try {
                updateTable(getPatientList(Main.getSessionUser().getEmployeeId()));
                uniButton.setText("New Referral");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PatientList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                try {
                    sendPatientToView(PatientList.getSelectionModel().getSelectedItem());
                    setPatientView();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public static void setView() throws Exception {
        Main.setCenterPane("PatientViews/PatientList.fxml");
    }

    public void updateTable() throws Exception {
        try {
            PatientList.setItems(getPatientList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        fillTable();
    }

    public void updateTable(ObservableList<Patient> patients) throws Exception {
        PatientList.setItems(patients);
        fillTable();
    }


    ////////////////////
    //Database Queries//
    ////////////////////


    ///////////////////
    //List Generators//
    ///////////////////
    @SuppressWarnings("Duplicates")
    public static ObservableList<Patient> getPatientList() throws Exception {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        ResultSet resultSet = Patient.queryAllPatients();
        while (resultSet.next()) {
            patients.add(new Patient(
                    resultSet.getInt("patient_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    dateFormatter(resultSet.getString("date_of_birth")),
                    resultSet.getString("sex"),
                    resultSet.getString("home_phone"),
                    resultSet.getString("email"),
                    resultSet.getString("insurance_number"),
                    resultSet.getString("policy_number"),
                    ""
            ));
        }
        return patients;
    }

    @SuppressWarnings("Duplicates")
    public ObservableList<Patient> getPatientList(int EmployeeID) throws Exception {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        ResultSet resultSet = Patient.queryPatients(EmployeeID);
        while (resultSet.next()) {
            patients.add(new Patient(
                    resultSet.getInt("patient_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    dateFormatter(resultSet.getString("date_of_birth")),
                    resultSet.getString("sex"),
                    resultSet.getString("home_phone"),
                    resultSet.getString("email"),
                    resultSet.getString("insurance_number"),
                    resultSet.getString("policy_number"),
                    ""
            ));
        }
        return patients;
    }


    //////////////////
    //Button Methods//
    //////////////////
    public void setAddPatientView()throws Exception{
        if(pms.contains(1)){
            ReferralFormController.setView();
        }
        else
            NewPatientController.setView();
    }

    public static void setPatientView() throws Exception {
        PatientViewController.setView();
    }


    ///////////////////
    //Form Validation//
    ///////////////////
    private static LocalDate dateFormatter(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, format);
    }

    private void sendPatientToView(Patient selectedItem) throws Exception {
        int patient_id = selectedItem.getPatientID();

        ResultSet rs = Patient.queryPatientInfo(patient_id);
        rs.next();

        ResultSet addr = Patient.queryAddress(rs.getInt("address_id"));
        addr.next();

        String address = addr.getString("street_name") + ", " + addr.getString("city") + ", " + addr.getString("state") + ", " + addr.getInt("zip");
        Main.setPatientFocus((new Patient(
                rs.getInt("patient_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                LocalDate.parse(rs.getString("date_of_birth")),
                rs.getString("sex"),
                rs.getString("home_phone"),
                rs.getString("email"),
                rs.getString("insurance_number"),
                rs.getString("policy_number"),
                address
        )));
    }


    private void fillTable() throws Exception {

        patientID.setCellValueFactory(new PropertyValueFactory<Patient, String>("patientID"));
        firstname.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastname"));
        dob.setCellValueFactory(new PropertyValueFactory<Patient, String>("dob"));
        sex.setCellValueFactory(new PropertyValueFactory<Patient, String>("sex"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("phoneNumber"));
        email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));

        FilteredList<Patient> sortedPatients = new FilteredList<>(getPatientList(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedPatients.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (patient.getFirstname().toLowerCase().contains(searched)) {
                    return true;
                } else if (patient.getLastname().toLowerCase().contains(searched)) {
                    return true;
                }
                else if (patient.getPhoneNumber().contains(searched)){
                    return true;
                }
                else if (patient.getEmail().contains(searched)){
                    return true;
                }
                else if (patient.getInsuranceNumber().contains(searched)){
                    return true;
                }
                else if (patient.getPolicyNumber().contains(searched)){
                    return true;
                }
                else if (patient.getAddress().toLowerCase().contains(searched)){
                    return true;
                }
                else if (Integer.toString(patient.getPatientID()).contains(searched)){
                    return true;
                }
               else if (patient.getDob().toString().contains(searched)){
                    return true;
                }
                return false;
            });
        });

        SortedList<Patient> sortedData = new SortedList<>(sortedPatients);

        sortedData.comparatorProperty().bind(PatientList.comparatorProperty());

        PatientList.setItems(sortedPatients);

    }
}
