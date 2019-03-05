package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class PatientController {

    private boolean EditPatientLock = false;

    public static void setPatientView()throws Exception{
        Main.setCenterPane("PatientViews/PatientView.fxml");
    }
    public static void setPatientList()throws Exception{
        Main.setCenterPane("PatientViews/PatientList.fxml");
    }
    public void setAddPatientView()throws Exception{
        Main.setPopupWindow("PatientViews/addPatient.fxml");
    }
    public void setBackPage()throws Exception{
        Main.setBackPage();
    }

      ///////////////////////
     //PatientList Methods//
    ///////////////////////
    public void setPatientView(ActionEvent actionEvent) throws Exception {
        this.setPatientView();
    }


      ///////////////////////
     //PatientView Methods//
    ///////////////////////
    @FXML TextField
              fNameField,    lNameField,      pNumberField,
              addressField,  dobField,        sNumberField,
              emailField,    InsuranceField,  balanceField;
    @FXML Button EditPatientInfoButton;

    //Allows the editing of the patient's information
    public void editPatientInfo(ActionEvent actionEvent) throws Exception {
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

    public void setAppointmentView(ActionEvent actionEvent) throws Exception {
        AppointmentController.setAppointmentView();
    }

    public void setAddAppointment(ActionEvent actionEvent) throws Exception {
        AppointmentController.setAddAppointment();
    }

      //////////////////////////
     //addPatientView Methods//
    //////////////////////////
    @FXML Button patientSubmit;
    @FXML CheckBox toggler;
    @FXML DatePicker scheduleDate;
    @FXML TableView scheduleTime;
    @FXML ComboBox appointmentCBox;
    public void toggleSchedule(){
        if (toggler.isSelected()){
          scheduleDate.setVisible(true);
          scheduleTime.setVisible(true);
          appointmentCBox.setVisible(true);
          Main.popup.setHeight(Main.popup.getHeight() + 240);
          patientSubmit.setLayoutY(patientSubmit.getLayoutY() + 290);
          patientSubmit.setLayoutX(patientSubmit.getLayoutX() + 18);
        }
        else {
          scheduleDate.setVisible(false);
          scheduleTime.setVisible(false);
          appointmentCBox.setVisible(false);
          Main.popup.setHeight( Main.popup.getHeight() - 240);
          patientSubmit.setLayoutY(patientSubmit.getLayoutY() - 290);
          patientSubmit.setLayoutX(patientSubmit.getLayoutX() - 18);
        }
      }
}
