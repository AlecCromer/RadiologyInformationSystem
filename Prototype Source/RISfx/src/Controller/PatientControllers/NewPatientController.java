package Controller.PatientControllers;

import Controller.Controller;
import Controller.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NewPatientController extends Controller {

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

    public static void setView()throws Exception{
        Main.setPopupWindow("PatientViews/addPatient.fxml");
    }
}
