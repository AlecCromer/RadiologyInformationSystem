package Controller.PatientControllers;

import Controller.Main;
import Model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NewPatientController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField     addressField, cityField, stateField, zipField,
                        fNameField, lNameField,  pNumberField, sNumberField,
                        emailField, insuranceField, policyField, sexField;
    @FXML Button        patientSubmit;
    @FXML CheckBox      toggler;
    @FXML DatePicker    scheduleDate, dobField;
    @FXML TableView     scheduleTime;
    @FXML ComboBox      appointmentCBox;
    @FXML
    AnchorPane pane;
    private static boolean valid = false;


      ////////////////
     //Initializers//
    ////////////////

    /**
     * sets DOB field to make people born before 1950 get anxious about their remaining expected lifespan
     * @param url
     * @param arg1
     */
    public void initialize(URL url, ResourceBundle arg1){
        dobField.setValue(LocalDate.of(1950, 1, 1));

    }

    /**
     * Opens addPatient.fxml as popup window, sets height and width
     * @throws Exception
     */
    public static void setView()throws Exception{
        Main.popup.setWidth(620);
        Main.popup.setHeight(300);
        Main.setPopupWindow("PatientViews/addPatient.fxml");
    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////

    /**
     * If the information entered in the edittexts in the fxml page passes form validation, submit a new patient object
     * with that information and exit the addPatient view
     * @throws Exception
     */
    @SuppressWarnings("Duplicates")
    public void submitNewPatient() throws Exception {
        if (checkField()== true){
            submitToDB();
        }

else{
        System.out.println("Invalid fields");}

    }
       public void submitToDB() throws Exception{
        Patient.insertNewPatient((new Patient(fNameField.getText(), lNameField.getText(), sexField.getText(), emailField.getText(),
                dobField.getValue(), pNumberField.getText(), insuranceField.getText(), policyField.getText())), addressField.getText(),
                cityField.getText(), stateField.getText(), zipField.getText()
        );

        Main.popup.close();
        Main.getOuter().setEffect(null);
        Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());
    }

    /**
     * Expands the add patient popup by setting new elements to be visible and modifying the popup window dimensions.
     */
    public void toggleSchedule(){
        if (toggler.isSelected()){
            scheduleDate.setVisible(true);
            scheduleTime.setVisible(true);
            appointmentCBox.setVisible(true);
            Main.popup.setHeight(Main.popup.getHeight() + 260);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() + 265);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() + 18);
        }
        else {
            scheduleDate.setVisible(false);
            scheduleTime.setVisible(false);
            appointmentCBox.setVisible(false);
            Main.popup.setHeight( Main.popup.getHeight() - 260);
            patientSubmit.setLayoutY(patientSubmit.getLayoutY() - 265);
            patientSubmit.setLayoutX(patientSubmit.getLayoutX() - 18);
        }
    }

    /**
     * Closes the popup window and refreshes the patient list view.
     * @throws Exception
     */
    private void exitView() throws Exception{
        Main.popup.close();
        Main.getOuter().setEffect(null);
        PatientListController.setView();
    }

    /**
     * formats the local date
     * @param date
     * @return
     */
    private String dateFormatter(LocalDate date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return date.format(format);
    }

    /**
     * Not implemented
     * @return
     */
    private void validateForm(){
        //TODO: Implement actual form validation
    }
      ///////////////////
     //Form Validation//
    ///////////////////
    /**
     * This is all the regex for the form validations in the add patient popup.
     * @return
     */
    @SuppressWarnings("Duplicates")
      private  boolean  checkField() {
            if(checkAgain() == true){
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
                if (!pNumberField.getText().matches("^1?[\\(\\- ]*\\d{3}[\\)-\\. ]*\\d{3}[-\\. ]*\\d{4}$")) {
                    error(4);
                } else {
                    error(5);
                }
                if (!addressField.getText().matches("^(?=.*[0-9])[a-zA-Z\\d\\s\\-#.+]+.*$")) {
                    error(6);
                } else {
                    error(7);
                }
                if (dobField.getValue() == null) {
                    error(8);
                } else {
                    error(9);
                }
                /*if (!sNumberField.getText().matches("^(?=.*[a-zA-Z]).*$")) { //Come back to this
                    error(10);
                } else {
                    error(11);
                }*/
                if (!emailField.getText().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) { //Was mapping out logic will clean up later
                    error(12);
                } else {
                    error(13);
                }
                if (!cityField.getText().matches("^(?=.*[a-zA-z]).*$")) {
                    error(14);
                } else {
                    error(15);
                }
                if (!stateField.getText().matches("^(?=.*[a-zA-z]).*$")) {
                    error(16);
                } else {
                    error(17);
                }
                if (!zipField.getText().matches("^(?=^.{5,5}$)(?=.*[0-9]).*$")) {
                    error(18);
                } else {
                    error(19);
                }
                if (!policyField.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")) {
                    error(20);
                } else {
                    error(21);
                }
                if (!sexField.getText().matches("^(?=.*[a-zA-Z]).*$")) {
                    error(22);
                } else {
                    error(23);
                }
                if (!insuranceField.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")) {
                    error(24);
                } else {
                    error(25);
                }
                System.out.println("Run again");
                return false;
            }

            return true;

      }



    /**
     * Outlines any field in the fxml that throws a form validation error in a red border.
     * @param fieldID
     */
    private void error ( int fieldID){

        switch (fieldID) {
            case 0: {
                fNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 1: {
                fNameField.setStyle(null);
                break;
            }
            case 2: {
                lNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 3: {
                lNameField.setStyle(null);
                break;
            }
            case 4: {
                pNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 5: {
                pNumberField.setStyle(null);
                break;
            }
            case 6: {
                addressField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 7: {
                addressField.setStyle(null);
                break;
            }
            case 8: {
                dobField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 9: {
                dobField.setStyle(null);
                break;
            }
            case 10: {
                sNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 11: {
                sNumberField.setStyle(null);
                break;
            }
            case 12: {
                emailField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 13: {
                emailField.setStyle(null);
                break;
            }
            case 14: {
                cityField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 15: {
                cityField.setStyle(null);
                break;
            }
            case 16: {
                stateField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 17: {
                stateField.setStyle(null);
                break;
            }
            case 18: {
                zipField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 19: {
                zipField.setStyle(null);
                break;
            }
            case 20: {
                policyField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 21: {
                policyField.setStyle(null);
                break;
            }
            case 22: {
                sexField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 23: {
                sexField.setStyle(null);
                break;
            }
            case 24: {
                insuranceField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                break;
            }
            case 25: {
                insuranceField.setStyle(null);
                break;
            }

        }

    }

    /**
     * Checks the validity of specified fields
     * @return
     */
    private boolean checkAgain() {

        if (fNameField.getText().matches("^(?=.*[a-zA-Z]).*$") && lNameField.getText().matches("^(?=.*[a-zA-Z]).*$")
                && pNumberField.getText().matches("^1?[\\(\\- ]*\\d{3}[\\)-\\. ]*\\d{3}[-\\. ]*\\d{4}$")
                && addressField.getText().matches("^(?=.*[0-9])[a-zA-Z\\d\\s\\-#.+]+.*$")
                && emailField.getText().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$") &&
                cityField.getText().matches("^(?=.*[a-zA-z]).*$") &&
                stateField.getText().matches("^(?=.*[a-zA-z]).*$") &&
                zipField.getText().matches("^(?=^.{5,5}$)(?=.*[0-9]).*$") &&
                policyField.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$") &&
                sexField.getText().matches("^(?=.*[a-zA-Z]).*$") &&
                insuranceField.getText().matches("^(?=^.{1,10}$)(?=.*[0-9]).*$")){

            return false;

        }
        else {
            return true;
        }
    }
}
