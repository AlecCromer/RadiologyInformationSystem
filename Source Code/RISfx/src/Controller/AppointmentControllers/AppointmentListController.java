package Controller.AppointmentControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TableView<Appointment>            AppointmentList;
    @FXML TableColumn<Appointment, Integer> appointmentID;
    @FXML TableColumn<Appointment, String>  patientFullName, DateTime, ProcedureType, Technician, Status, Balance;
    @FXML private TextField searchField;
      ////////////////
     //Initializers//
    ////////////////

    /**
     * sets AppointmentList.fxml to center pane.
     * @throws Exception
     */
    public static void setView()throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentList.fxml");
    }

    /**
     * Executes updateTable() to populate appointments table
     * Has onClick method for a cell in this table that opens the AppointmentView.fxml via AppointmentViewController.setView()
     * @param url
     * @param arg1
     */
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppointmentList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL

            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    sendAppointmentToView(AppointmentList.getSelectionModel().getSelectedItem());
                    AppointmentViewController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Executes getAppointmentList() in this file
     * uses attributes of appointment objects in returned list to populate cell values and add to table
     */
    @SuppressWarnings("Duplicates")
    private void updateTable() throws Exception {
        try {

            AppointmentList.setItems(getAppointmentList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        patientFullName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientFullName"));
        DateTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
        ProcedureType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("procedureName"));
        Technician.setCellValueFactory(new PropertyValueFactory<Appointment, String>("technician"));
        Status.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientStatus"));
        Balance.setCellValueFactory(new PropertyValueFactory<Appointment, String>("balance"));

        FilteredList<Appointment> sortedAppointments = new FilteredList<>(getAppointmentList(), p -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedAppointments.setPredicate(appointments -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (appointments.getPatientFullName().toLowerCase().contains(searched)) {
                    return true;
                } else if (appointments.getProcedureName().toLowerCase().contains(searched)) {
                    return true;
                }
                else if (appointments.getTechnician().toLowerCase().contains(searched)){
                    return true;
                }
                else if (appointments.getPatientStatus().toLowerCase().contains(searched)){
                    return true;
                }
                else if (Float.toString(appointments.getBalance()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(appointments.getAppointmentId()).contains(searched)){
                    return true;
                }
               else if (appointments.getAppointmentDate().toString().contains(searched)){
                    return true;
                }
                return false;
            });
        });

        SortedList<Appointment> sortedData = new SortedList<>(sortedAppointments);

        sortedData.comparatorProperty().bind(AppointmentList.comparatorProperty());

        AppointmentList.setItems(sortedData);
    }


      ///////////////////
     //List Generators//
    ///////////////////

    /**
     * Calls Appointment.queryAppointments() to get resultset of appointments from SQL database.
     * makes appointment objects using constructors from Appointment class
     * returns list
     * @return
     * @throws Exception
     */
    private ObservableList<Appointment> getAppointmentList() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(ResultSet resultSet = Appointment.queryAppointments()){
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

    /**
     * Runs AppointmentViewController.setView() to get AppointmentView.fxml
     * @param actionEvent
     * @throws Exception
     */
    public void setAppointmentView(ActionEvent actionEvent) throws Exception{
        AppointmentViewController.setView();
    }

    /**
     * Reset the Patient Focus when pressing this button to clear the patient ID Field in next view
     * @param actionEvent
     * @throws Exception
     */
    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        Main.setPatientFocus(new Patient());
        AddAppointmentController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////

    /**
     * Form validation done through generateAppointmentFocus(rs)
     * @param selectedItem
     * @throws Exception
     */
    private void sendAppointmentToView(Appointment selectedItem) throws Exception{
        int appointmentId = selectedItem.getAppointmentId();
        ResultSet rs = Appointment.queryAppointmentFocus(appointmentId);
        rs.next();
        Main.setAppointmentFocus(Appointment.generateAppointmentFocus(rs));
    }
}
