package Controller.ReportControllers;

import Controller.Main;
import Model.Patient;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportListController implements Initializable{

    ////////////////////////
    //Variable Declaration//
    ////////////////////////

    @FXML private TableView<Report>            ReportList;
    @FXML private TableColumn<Report, String>  patientID, firstname, lastname, dob, sex;
    @FXML private Button incomplete;
    @FXML
    private TextField searchField;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    private String search;

    ////////////////
    //Initializers//
    ////////////////

    public void initialize(URL url, ResourceBundle arg1) {
        setSearch("Needs Review");
        ArrayList pms = Main.getSessionUser().getPermissions();
        if(pms.contains(2)){
            try {
                incomplete.setVisible(false);
                updateTable(getPatientList(Main.getSessionUser().getEmployeeId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(pms.contains(1)){
            try {
                updateTable(getPatientList(Main.getSessionUser().getEmployeeId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                incomplete.setVisible(true);
                updateTable(getPatientList());
            }catch (Exception e){}
            ReportList.setOnMouseClicked((MouseEvent event) -> {
                //DOUBLE CLICK ON CELL
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    try {
                        Report item = ReportList.getSelectionModel().getSelectedItem();
                        ReportFormController.setView(item.getAppointment_id(), item.getImage_id());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void setView() throws Exception {
        Main.setCenterPane("ReportViews/ReportList.fxml");
    }

    @SuppressWarnings("Duplicates")
    public void updateTable(ObservableList<Report> patientObservableList) throws Exception {
        try {

            ReportList.setItems(patientObservableList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        patientID.setCellValueFactory(new PropertyValueFactory<Report, String>("patient_id"));
        firstname.setCellValueFactory(new PropertyValueFactory<Report, String>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<Report, String>("lastname"));
        dob.setCellValueFactory(new PropertyValueFactory<Report, String>("dob"));
        sex.setCellValueFactory(new PropertyValueFactory<Report, String>("sex"));

        FilteredList<Report> sortedReport= new FilteredList<>(getPatientList(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedReport.setPredicate(report -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (report.getFirstname().toLowerCase().contains(searched)) {
                    return true;
                } else if (report.getLastname().toLowerCase().contains(searched)) {
                    return true;
                }
                else if (report.getSex().toLowerCase().contains(searched)){
                    return true;
                }
                else if (report.getDob().toString().contains(searched)){
                    return true;
                }

                else if (report.getPatient_id().contains(searched)){
                    return true;
                }

                return false;
            });
        });

        SortedList<Report> sortedData = new SortedList<>(sortedReport);

        sortedData.comparatorProperty().bind(ReportList.comparatorProperty());

        ReportList.setItems(sortedReport);

    }

    ////////////////////
    //Database Queries//
    ////////////////////
    public void completeList() throws Exception {
        setSearch("Complete");
        ArrayList pms = Main.getSessionUser().getPermissions();
        if (pms.contains(2)) {
            updateTable(getPatientList(Main.getSessionUser().getEmployeeId()));
        } else {
            updateTable(getPatientList());
        }
    }

    public void incompleteList() throws Exception{
        setSearch("Needs Review");
        ArrayList pms = Main.getSessionUser().getPermissions();
        if (pms.contains(2)) {
            updateTable(getPatientList(Main.getSessionUser().getEmployeeId()));
        } else {
            updateTable(getPatientList());
        }
    }
    ///////////////////
    //List Generators//
    ///////////////////
    @SuppressWarnings("Duplicates")
    public ObservableList<Report>  getPatientList() throws Exception {
        ObservableList<Report> reports = FXCollections.observableArrayList();

        ResultSet resultSet = Report.queryReports(getSearch());
        while (resultSet.next()) {
            reports.add(new Report(
                    resultSet.getString("p.patient_id"),
                    resultSet.getString("p.first_name"),
                    resultSet.getString("p.last_name"),
                    dateFormatter(resultSet.getString("p.date_of_birth")),
                    resultSet.getString("p.sex"),
                    resultSet.getString("image_id"),
                    resultSet.getInt("appointment_id")
            ));
        }
        return reports;
    }
    @SuppressWarnings("Duplicates")
    public ObservableList<Report>  getPatientList(int employeeID) throws Exception {
        ObservableList<Report> reports = FXCollections.observableArrayList();

        ResultSet resultSet = Report.queryReports(getSearch(), employeeID);
        while (resultSet.next()) {
            reports.add(new Report(
                    resultSet.getString("p.patient_id"),
                    resultSet.getString("p.first_name"),
                    resultSet.getString("p.last_name"),
                    dateFormatter(resultSet.getString("p.date_of_birth")),
                    resultSet.getString("p.sex"),
                    resultSet.getString("image_id"),
                    resultSet.getInt("appointment_id")
            ));
        }
        return reports;
    }

    private LocalDate dateFormatter (String date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, format);
    }

}

