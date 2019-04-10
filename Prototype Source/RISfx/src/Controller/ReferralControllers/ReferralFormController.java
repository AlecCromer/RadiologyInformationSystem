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
                    patientEmail,       patientInsuranceNumber, patientPolicyNumber,
                    heartRate, diastolicPressure, systolicPressure, weight, height;

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
                          patientInsuranceNumber.getText(),
                          patientPolicyNumber.getText()
                  )),
                  patientStreet.getText(),
                  patientCity.getText(),
                  patientState.getText(),
                  patientZip.getText()
            );
            //This is a pre set value, shouldn't this change depending on who logs in?
        int employeeID = Main.getSessionUser().getEmployeeId();

        Referral.insertNewReferral(patientID, employeeID, comboSelection, urgencyBox.getValue(), referralReason.getText(), referralComments.getText(), Integer.parseInt(height.getText()),  Integer.parseInt(weight.getText()),  Integer.parseInt(heartRate.getText()),  Integer.parseInt(systolicPressure.getText()),  Integer.parseInt(diastolicPressure.getText()));
        Main.popup.close();
        Main.getOuter().setDisable(false);
        Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());

    }

     ///////////////////
     //Form Validation//
     ///////////////////
// Do we want to reject a field if they don't have an upper case
     private void checkField(){
         if (!patentFirstName.getText().matches("^(?=.*[a-zA-Z]).*$")){
             error(0);
         }
         else{
             error(1);
         }
         if (!patientLastName.getText().matches("^(?=.*[a-zA-Z]).*$")){
             error(2);
         }
         else{
             error(3);
         }
         if(!patientStreet.getText().matches("^(?=.*[0-9])[a-zA-Z\\d\\s\\-#.+]+.*$")){
             error(4);
         }
         else{
             error(5);
         }
         if(!patientSex.getText().matches("^(?=.*[a-zA-Z]).*$")){
             error(6);
         }
         else{
             error(7);
         }
         if (!patientCity.getText().matches("^(?=.*[a-z])+(?=.*[A-Z]).*$")){
             error(8);
         }
        else{
            error(9);
         }
        if(!patientState.getText().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")){ //Come back to this
            error(10);
        }
        else{
            error(11);
        }
        if(!patientZip.getText().matches("^(?=^.{5,5}$)(?=.*[0-9]).*$")){
            error(12);
         }
        else{
            error(13);
        }
        if(!patientPhone.getText().matches("^1?[\\(\\- ]*\\d{3}[\\)-\\. ]*\\d{3}[-\\. ]*\\d{4}$")){ //Was mapping out logic will clean up later
            error(14);
        }
        else{
            error(15);
        }
        if(!patientEmail.getText().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")){
            error(16);
        }
        else{
            error(17);
        }
        if(!patientInsuranceNumber.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")){
            error(18);
        }
        else{
            error(19);
        }
        if(!patientPolicyNumber.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")){
            error(20);
        }
        else{
            error(21);
        }
        if(!heartRate.getText().matches("^[0-9]{1,4}+$")){
            error(22);
        }
        else{
            error(23);
        }
        //Do conditions need to be added or is this fine as is?
        if(procedureBox.getSelectionModel().isEmpty()){
            error(24);
        }
        else{
            error(25);
        }
        if(urgencyBox.getSelectionModel().isEmpty()){
            error(26);
        }
        else {
            error(27);
        }
        if(referralReason.getText().isEmpty()){
            error(28);
        }
        else{
            error(29);
        }
        if (referralComments.getText().isEmpty()){
            error(30);
        }
        else{
            error(31);
        }
        if(patientDoB.getValue() == null){
            error(32);
        }
        else{
            error(33);
        }
         if(!systolicPressure.getText().matches("^[0-9]{1,4}+$")){
             error(34);
         }
         else{
             error(35);
         }

         if(!height.getText().matches("^[0-9]{1,4}+$")){
             error(36);
         }
         else{
             error(37);
         }
         if(!weight.getText().matches("^[0-9]{1,4}+$")){
             error(38);
         }
         else{
             error(39);
         }
         if(!diastolicPressure.getText().matches("^[0-9]{1,4}+$")){
             error(40);
         }
         else{
             error(41);
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
                patientStreet.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 5: {
                patientStreet.setStyle(null);
                return;
            }
            case 6: {
                patientSex.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 7: {
                patientSex.setStyle(null);
                return;
            }
            case 8: {
                patientCity.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 9: {
                patientCity.setStyle(null);
                return;
            }
            case 10: {
                patientState.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 11: {
                patientState.setStyle(null);
                return;
            }
            case 12: {
                patientZip.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 13: {
                patientZip.setStyle(null);
                return;
            }
            case 14: {
                patientPhone.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 15: {
                patientPhone.setStyle(null);
                return;
            }
            case 16: {
                patientEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 17: {
                patientEmail.setStyle(null);
                return;
            }
            case 18: {
                patientInsuranceNumber.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 19: {
                patientInsuranceNumber.setStyle(null);
                return;
            }
            case 20: {
                patientPolicyNumber.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 21: {
                patientPolicyNumber.setStyle(null);
                return;
            }
            case 22: {
                heartRate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 23: {
                heartRate.setStyle(null);
                return;
            }
            case 24: {
                procedureBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 25: {
                procedureBox.setStyle(null);
                return;
            }
            case 26: {
                urgencyBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 27: {
                urgencyBox.setStyle(null);
                return;
            }
            case 28: {
                referralReason.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 29: {
                referralReason.setStyle(null);
                return;
            }
            case 30: {
                referralComments.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 31: {
                referralComments.setStyle(null);
                return;
            }
            case 32: {
                patientDoB.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 33: {
                patientDoB.setStyle(null);
                return;
            }
            case 34: {
                systolicPressure.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 35: {
                systolicPressure.setStyle(null);
                return;
            }
            case 36: {
                height.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 37: {
                height.setStyle(null);
                return;
            }
            case 38: {
                weight.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 39: {
                weight.setStyle(null);
                return;
            }
            case 40: {
                diastolicPressure.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                return;
            }
            case 41: {
                diastolicPressure.setStyle(null);
                return;
            }
        }
}

}
