package Controller.ReportControllers;

import Controller.Main;
import Model.Report;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportListController implements Initializable{

    ////////////////////////
    //Variable Declaration//
    ////////////////////////

    @FXML private TableView<Report>            ReportList;
    @FXML private TableColumn<Report, String>  patientID, firstname, lastname, dob, sex;

    ////////////////
    //Initializers//
    ////////////////

    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
        updateTable();
        ReportList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                try {
                    //sendProcedureToView(ProcedureList.getSelectionModel().getSelectedItem());
                    //setProcedureView();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setView() throws Exception {
        Main.setCenterPane("ReportViews/ReportList.fxml");
    }

    public void updateTable() {
        try {

            ReportList.setItems(getPatientList());
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
    }

    ////////////////////
    //Database Queries//
    ////////////////////


    ///////////////////
    //List Generators//
    ///////////////////

    ///////////////////
    //List Generators//
    ///////////////////
    public ObservableList<Report>  getPatientList() throws Exception {
        ObservableList<Report> reports = FXCollections.observableArrayList();
/*
        ResultSet resultSet = Report.queryReports();
        while (resultSet.next()) {
            reports.add(new Report(
                    resultSet.getString("p.patient_id"),
                    resultSet.getString("p.first_name"),
                    resultSet.getString("p.last_name"),
                    dateFormatter(resultSet.getString("p.date_of_birth")).toString(),
                    resultSet.getString("p.sex")
            ));

        }*/
        return reports;
    }


        private LocalDate dateFormatter (String date){
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, format);
        }

    }
