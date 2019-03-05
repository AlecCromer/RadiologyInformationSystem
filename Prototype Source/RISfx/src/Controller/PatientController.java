package Controller;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class PatientController  implements Initializable {

    ///////////////////////
    //PatientView Methods//
    ///////////////////////

    @FXML
    private TableView<Patient> PatientList;

    @FXML
    private TableColumn<Patient, String> patientID, firstname, lastname, dob, sex, pnumber, email;


    public void initialize(URL url, ResourceBundle arg1) {


        //setSQLQuery("select title, description, content FROM item");
        patientListFill("select PatientID, firstname, lastname, DoB, Sex, pnumber, email FROM patientlist");
    }



    public static void setPatientView()throws Exception{
        Main.setCenterPane("PatientViews/PatientView.fxml");
    }


    public static void setPatientList()throws Exception{
        Main.setCenterPane("PatientViews/PatientList.fxml");
    }

    public void patientListFill(String string) {
        try {

            PatientList.setItems(getPatientList(string));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
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
        patientID.setCellValueFactory(new PropertyValueFactory<Patient, String>("patientID"));
        System.out.println(new PropertyValueFactory<Patient, String>("patientID"));
        firstname.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastname"));
        dob.setCellValueFactory(new PropertyValueFactory<Patient, String>("dob"));
        sex.setCellValueFactory(new PropertyValueFactory<Patient, String>("sex"));
        pnumber.setCellValueFactory(new PropertyValueFactory<Patient, String>("pumber"));
        email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));
    }

    public ObservableList<Patient>/*<String>*/  getPatientList(String SQL) throws IOException
    {
        ObservableList<Patient>/*<String>*/ patients = FXCollections.observableArrayList();

        try(
                Connection conn = databaseConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(SQL);
                ResultSet resultSet = displayprofile.executeQuery();

        ){
            while (resultSet.next()){
                patients.add(new Patient(resultSet.getInt("PatientID"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("DoB"), resultSet.getString("Sex"), resultSet.getString("pnumber"), resultSet.getString("email")));


            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return patients;


    }



    public void setAddPatientView()throws Exception{
        Main.setPopupWindow("PatientViews/addPatient.fxml");
    }


      ///////////////////////
     //PatientList Methods//
    ///////////////////////
    public void setPatientView(ActionEvent actionEvent) throws Exception {
        this.setPatientView();
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
