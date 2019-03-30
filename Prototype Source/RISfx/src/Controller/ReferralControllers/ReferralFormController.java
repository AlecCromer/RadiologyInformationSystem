package Controller.ReferralControllers;

import Controller.Main;
import Model.Patient;
import Model.Procedure;
import Model.Referral;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ReferralFormController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField patentFirstName,    patientLastName,        patientStreet,          patientSex,
                    patientCity,        patientState,           patientZip,             patientPhone,
                    patientEmail,       patientInsuranceNumber, patientPolicyNumber,    patientVitals;

    @FXML ComboBox<String>  procedureBox, urgencyBox;
    @FXML TextArea          referralReason, referralComments;
    @FXML DatePicker        patientDoB;
    private int comboSelection;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.setPopupWindow("ReferralViews/ReferralForm.fxml");
        Main.popup.setResizable(false);
        Main.popup.setHeight(550);
        Main.popup.setWidth(600);
    }

    public void initialize(URL url, ResourceBundle arg1) {
        try {
            ObservableList<String> procedureList = Procedure.getProcedureList();
            ObservableList<String> urgencyList = FXCollections.observableArrayList();

            urgencyList.add("Low");
            urgencyList.add("Medium");
            urgencyList.add("High");
            urgencyList.add("Emergency");

            urgencyBox.setItems(urgencyList);
            procedureBox.setItems(procedureList);
            procedureBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    comboSelection = Integer.parseInt(procedureBox.getValue().split(": ")[0]);
                }catch (Exception e) { e.printStackTrace(); }
            });
        }
        catch (Exception e){ e.printStackTrace(); }


    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    //Suppress warning about our method call being similar to another bit
    @SuppressWarnings("Duplicates")
    public void submitNewReferral() throws Exception{
        checkField();
        int patientID = Patient.insertNewPatient((new Patient(
                          patentFirstName.getText(),
                          patientLastName.getText(),
                          patientSex.getText(),
                          patientEmail.getText(),
                          patientDoB.getValue(),
                          patientPhone.getText(),
                          Integer.parseInt(patientInsuranceNumber.getText()),
                          Integer.parseInt(patientPolicyNumber.getText())
                  )),
                  patientStreet.getText(),
                  patientCity.getText(),
                  patientState.getText(),
                  patientZip.getText()
            );

        int employeeID = 12442;

        Referral.insertNewReferral(patientID, employeeID, comboSelection, urgencyBox.getValue(), referralReason.getText(), referralComments.getText());
        checkField();
        Main.popup.close();
        Main.getOuter().setDisable(false);
        Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());
    }

      ///////////////////
     //Form Validation//
    ///////////////////
//    admin@admin.com
 private void checkField(){
     if (patentFirstName.getText().isEmpty()) {
         error(0);
      else if(patentFirstName.getText().matches("[a-z A-Z]+")) {
             error(0);
         }
     }
     if (patientLastName.getText().isEmpty()){
         error(2);
         if (patientLastName.getText().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")) {
             error(2);
         }
     }
      else {
         error(4);
     }
 }
    private void error(int fieldID) {


        switch (fieldID) {
            case 0: {
                patentFirstName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 1: {
                patentFirstName.setStyle(null);
                return;
            }
            case 2: {
                patientLastName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 3: {
                patientLastName.setStyle(null);
                return;
            }
            case 4: {
                patentFirstName.setStyle(null);
                patientLastName.setStyle(null);
                return;
            }
        }
}
}
