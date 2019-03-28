package Controller.TechControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WorkListController implements Initializable {

    ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML
    TableView<Appointment>              WorkList;
    @FXML
    TableColumn<Appointment, Integer>   appointmentID;
    @FXML
    TableColumn<Appointment, String>    patientFullName,    appointmentDateTime,    procedureName,
                                        patientStatus,      appointmentBalance;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView()throws Exception{
        Main.setCenterPane("TechViews/WorkList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
        WorkList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendAppointmentToView(WorkList.getSelectionModel().getSelectedItem());
                    TechEntryController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateTable() {
        try {
           WorkList.setItems(getAppointmentList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        patientFullName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientFullName"));
        appointmentDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
        procedureName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("procedureName"));
        patientStatus.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        //appointmentBalance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));
    }


      ///////////////////
     //List Generators//
    ///////////////////
    private ObservableList<Appointment> getAppointmentList() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet resultSet = Appointment.queryWorkList(12442)){
            while (resultSet.next()){
                appointments.add(Appointment.generateAppointmentFocus(resultSet));
            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return appointments;
    }


      //////////////////
     //Button Methods//
    //////////////////


      ///////////////////
     //Form Validation//
    ///////////////////
    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();
        ResultSet rs = Appointment.queryAppointmentFocus(appointmentId);
        rs.next();
        Main.setAppointmentFocus(Appointment.generateAppointmentFocus(rs));
    }
}
