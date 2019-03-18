package Controller.ReferralControllers;

import Controller.AppointmentControllers.AppointmentViewController;
import Controller.Main;
import Controller.databaseConnector;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReferralListController implements Initializable {
      ////////////////////////
     //Variable Declaration//
    ////////////////////////
      @FXML TableView<Referral>         ReferralList;
      @FXML
      TableColumn<Referral, Integer>    patientID;
      @FXML
      TableColumn<Referral, String>     patientFirstName,   patientLastName,    referrerFullName,
                                        procedureName,      patientPhone,       urgency;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.setCenterPane("ReferralViews/ReferralList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        }catch (Exception e){e.printStackTrace();}
        ReferralList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendReferralToView(ReferralList.getSelectionModel().getSelectedItem());
                    AppointmentViewController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateTable() throws Exception{
        ReferralList.setItems(generateRefferalsList());
        patientID.setCellValueFactory(new PropertyValueFactory<Referral, Integer>("patientID"));
        patientFirstName.setCellValueFactory(new PropertyValueFactory<Referral, String>("patientFirstName"));
        patientLastName.setCellValueFactory(new PropertyValueFactory<Referral, String>("patientLastName"));
        referrerFullName.setCellValueFactory(new PropertyValueFactory<Referral, String>("referrerFullName"));
        procedureName.setCellValueFactory(new PropertyValueFactory<Referral, String>("procedureName"));
        patientPhone.setCellValueFactory(new PropertyValueFactory<Referral, String>("patientPhone"));
        urgency.setCellValueFactory(new PropertyValueFactory<Referral, String>("urgency"));
    }


      ///////////////////
     //List Generators//
    ///////////////////
    private ObservableList<Referral> generateRefferalsList() throws Exception{
        ObservableList<Referral> referrals = FXCollections.observableArrayList();

        try(ResultSet resultSet = Referral.queryUnprocessedReferrals()){
            while (resultSet.next()){
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
                        resultSet.getInt("home_phone")
                    ),
                    resultSet.getBoolean("is_processed"),
                    resultSet.getString("urgency")
                ));
            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return referrals;
    }


      //////////////////
     //Button Methods//
    //////////////////
    public void setReferralForm(ActionEvent actionEvent) throws Exception{
        ReferralFormController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    public void sendReferralToView(Referral referralToSend) throws Exception{
          Main.setPopupWindow("ReferralViews/addReferredPatient.fxml");
          Main.popup.setResizable(false);
          Main.popup.setHeight(550);
          Main.popup.setWidth(520);
    }
}
