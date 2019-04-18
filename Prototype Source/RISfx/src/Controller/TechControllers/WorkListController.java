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

/**
 * WorkListController is used to create a list of tasks that a technician needs to work on
 */
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

    /**
     * Used to set the view to the work list
     * @throws Exception if changing the view returns an error
     */
    public static void setView()throws Exception{
        Main.setCenterPane("TechViews/WorkList.fxml");
    }

    /**
     * Initializes the work list using scheduled appointments
     * @param url the location for relative paths
     * @param arg1 the resources used to localize the root object
     */
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

    /**
     * Sets the table with data about the appointment
     * @throws Exception if the SQL returns an error
     */
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


    /**
     * Used to return a ObservableList of the work list information for the employee logged in
     * @return ObservableList with work list information
     * @throws Exception if the SQL returns an error
     */
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


    /**
     * Sends the appointment data for the tech entry controller
     * @param selectedItem The selected appointment
     * @throws Exception if the SQL returns an error
     */
    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();
        ResultSet rs = Appointment.queryAppointmentFocus(appointmentId);
        rs.next();
        Main.setAppointmentFocus(Appointment.generateAppointmentFocus(rs));
    }
}
