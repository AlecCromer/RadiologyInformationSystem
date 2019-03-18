package Controller.ReferralControllers;

import Controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ReferralFormController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField patentFirstName,    patientLastName,        patientDoB,             patientStreet,
                    patientCity,        patientState,           patientZip,             patientPhone,
                    patientEmail,       patientInsuranceNumber, patientPolicyNumber,    patientVitals;

    @FXML ComboBox<String>  procedureBox;
    @FXML TextArea          referralSummary;

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

    }

    private void updateTable() {

    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////


      ///////////////////
     //Form Validation//
    ///////////////////


}
