package Controller.ReferralControllers;

import Controller.AppointmentControllers.AddAppointmentController;
import Controller.Main;
import Controller.databaseConnector;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReferralListController implements Initializable {
    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML
    TableView<Referral> ReferralList;
    @FXML
    TableColumn<Referral, Integer> patientID;
    @FXML
    TableColumn<Referral, String> patientFirstName, patientLastName, referrerFullName,
            procedureName, patientPhone, urgency, height, weight, heart_rate, systolic, diastolic;
    @FXML
    AnchorPane referralView;
   @FXML private TextField searchField;

    ////////////////
    //Initializers//
    ////////////////
    /**
     * Sets the view with the Referral list
     */
    public static void setView() throws Exception {
        Main.setCenterPane("ReferralViews/ReferralList.fxml");
    }
    /**
     *Populates the table and by double clicking on a cell
     *it pulls up the add appointment view
     */
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReferralList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                try {
                    Main.getPatientFocus().setPatientID(ReferralList.getSelectionModel().getSelectedItem().getPatientID());
                    AddAppointmentController.setView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     *Populates the table with data from the database and
     *Search method takes the filled data and filters it and then loads it back into the
     *table to allow the user to sort through the table
     */
@SuppressWarnings("Dupilcates")
    private void updateTable() throws Exception {
        ReferralList.setItems(generateRefferalsList());
        patientID.setCellValueFactory(new PropertyValueFactory<Referral, Integer>("patientID"));
        patientFirstName.setCellValueFactory(new PropertyValueFactory<Referral, String>("patientFirstName"));
        patientLastName.setCellValueFactory(new PropertyValueFactory<Referral, String>("patientLastName"));
        referrerFullName.setCellValueFactory(new PropertyValueFactory<Referral, String>("referrerFullName"));
        procedureName.setCellValueFactory(new PropertyValueFactory<Referral, String>("procedureName"));
        patientPhone.setCellValueFactory(new PropertyValueFactory<Referral, String>("patientPhone"));
        urgency.setCellValueFactory(new PropertyValueFactory<Referral, String>("urgency"));
        height.setCellValueFactory(new PropertyValueFactory<Referral, String>("height"));
        weight.setCellValueFactory(new PropertyValueFactory<Referral, String>("weight"));
        heart_rate.setCellValueFactory(new PropertyValueFactory<Referral, String>("heart_rate"));
        systolic.setCellValueFactory(new PropertyValueFactory<Referral, String>("systolic_pressure"));
        diastolic.setCellValueFactory(new PropertyValueFactory<Referral, String>("diastolic_pressure"));

        FilteredList<Referral> sortedRefferals = new FilteredList<>(generateRefferalsList(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedRefferals.setPredicate(referrals -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (referrals.getPatientFirstName().toLowerCase().contains(searched)) {
                    return true;
                } else if (referrals.getPatientLastName().toLowerCase().contains(searched)) {
                    return true;
                }
                else if (referrals.getPatientPhone().contains(searched)){
                    return true;
                }
                else if (referrals.getProcedureName().toLowerCase().contains(searched)){
                    return true;
                }
                else if (referrals.getReferrerFullName().toLowerCase().contains(searched)){
                    return true;
                }
                else if (referrals.getUrgency().toLowerCase().contains(searched)){
                    return true;
                }
                else if (Integer.toString(referrals.getDiastolic_pressure()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(referrals.getHeart_rate()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(referrals.getHeight()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(referrals.getSystolic_pressure()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(referrals.getWeight()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(referrals.getPatientID()).contains(searched)){
                    return true;
                }
                return false;
            });
        });

        SortedList<Referral> sortedData = new SortedList<>(sortedRefferals);

        sortedData.comparatorProperty().bind(ReferralList.comparatorProperty());

        ReferralList.setItems(sortedRefferals);

    }


    ///////////////////
    //List Generators//
    ///////////////////
    /**
     *Gets data from the database so it can be loaded into the table
     *
     */
    private ObservableList<Referral> generateRefferalsList() throws Exception {
        ObservableList<Referral> referrals = FXCollections.observableArrayList();

        try (ResultSet resultSet = Referral.queryUnprocessedReferrals()) {
            while (resultSet.next()) {
                referrals.add(new Referral(
                        new Procedure(
                                resultSet.getInt("procedure_id"),
                                resultSet.getInt("procedure_length"),
                                resultSet.getString("procedure_name")
                        ),
                        new Employee(
                                resultSet.getInt("employee_id"),
                                resultSet.getString("referrer_first_name"),
                                resultSet.getString("referrer_last_name"),
                                resultSet.getString("referrer_email")
                        ),
                        new Patient(
                                resultSet.getInt("patient_id"),
                                resultSet.getString("patient_first_name"),
                                resultSet.getString("patient_last_name"),
                                resultSet.getString("home_phone")
                        ),
                        resultSet.getBoolean("is_processed"),
                        resultSet.getString("urgency"),
                        resultSet.getInt("height"),
                        resultSet.getInt("weight"),
                        resultSet.getInt("heart_rate"),
                        resultSet.getInt("systolic_pressure"),
                        resultSet.getInt("diastolic_pressure")
                ));
            }
        } catch (SQLException ex) {
            databaseConnector.displayException(ex);
            System.out.println("Unable to process the request.");
            return null;
        }
        return referrals;
    }


    //////////////////
    //Button Methods//
    //////////////////
    /**
     *Button that sets the view to the Referral form when it is pressed
     */
    public void setReferralForm(ActionEvent actionEvent) throws Exception {
        ReferralFormController.setView();
    }


    ///////////////////
    //Form Validation//
    ///////////////////



}
