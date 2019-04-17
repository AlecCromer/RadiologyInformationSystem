package Controller.TechControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WorkListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML Text loggedIn;
    @FXML
    TableView<Appointment>              WorkList;
    @FXML
    TableColumn<Appointment, Integer>   appointmentID;
    @FXML
    TableColumn<Appointment, String>    patientFullName,    appointmentDateTime,    procedureName,
            patientStatus,      appointmentBalance;
    @FXML
    private TextField searchField;
    ////////////////
    //Initializers//
    ////////////////
    public static void setView()throws Exception{
        Main.setCenterPane("TechViews/WorkList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        loggedIn.setText(loggedIn.getText() + Main.getSessionUser().getFullName());
    }

    private void updateTable() throws Exception {
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
        appointmentBalance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));

        FilteredList<Appointment> sortedWorklist = new FilteredList<>(getAppointmentList(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedWorklist.setPredicate(appointment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (appointment.getPatientFullName().toLowerCase().contains(searched)) {
                    return true;
                } else if (Float.toString(appointment.getBalance()).contains(searched)) {
                    return true;
                }
                else if (appointment.getPatientStatus().toLowerCase().contains(searched)){
                    return true;
                }
                else if (appointment.getProcedureName().toLowerCase().contains(searched)){
                    return true;
                }
                else if (Integer.toString(appointment.getAppointmentId()).contains(searched)){
                    return true;
                }
               else if (appointment.getAppointmentDate().toString().contains(searched)){
                    return true;
                }
                return false;
            });
        });


        SortedList<Appointment> sortedData = new SortedList<>(sortedWorklist);

        sortedData.comparatorProperty().bind(WorkList.comparatorProperty());

        WorkList.setItems(sortedWorklist);
    }


    ///////////////////
    //List Generators//
    ///////////////////
    private ObservableList<Appointment> getAppointmentList() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet resultSet = Appointment.queryWorkList(Main.getSessionUser().getEmployeeId())){
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
