package Controller.ProcedureControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Patient;
import Model.Procedure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import Controller.ProcedureControllers.AddProcedureController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProcedureListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////

    @FXML private TableView<Procedure>            ProcedureList;
    @FXML private TableColumn<Procedure, String>  procedureID, procedureName;
    @FXML private TableColumn<Procedure, Integer>   procedureLength;
    @FXML private TableColumn<Procedure, Float>     procedureCost;

      ////////////////
     //Initializers//
    ////////////////

    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
        updateTable();
        ProcedureList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    //sendProcedureToView(ProcedureList.getSelectionModel().getSelectedItem());
                    //setProcedureView();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setView()throws Exception{
        Main.setCenterPane("ProcedureViews/ProcedureList.fxml");
    }

    public void updateTable() {
        try {

            ProcedureList.setItems(getProcedureList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        procedureID.setCellValueFactory(new PropertyValueFactory<Procedure, String>("procedureId"));
        procedureName.setCellValueFactory(new PropertyValueFactory<Procedure, String>("procedureName"));
        procedureLength.setCellValueFactory(new PropertyValueFactory<Procedure, Integer>("procedureLength"));
        procedureCost.setCellValueFactory(new PropertyValueFactory<Procedure, Float>("price"));
    }

      ////////////////////
     //Database Queries//
    ////////////////////



      ///////////////////
     //List Generators//
    ///////////////////

    public ObservableList<Procedure>  getProcedureList() throws Exception {
        ObservableList<Procedure> procedures = FXCollections.observableArrayList();

        ResultSet resultSet = Procedure.queryAllProcedures();
        while (resultSet.next()) {
            procedures.add(new Procedure(
                    resultSet.getInt("procedure_id"),
                    resultSet.getString("procedure_name"),
                    resultSet.getInt("procedure_length"),
                    resultSet.getFloat("procedure_price")
            ));
        }
        return procedures;
    }

    //////////////////
    //Button Methods//
    //////////////////
    public void setAddProcedureView()throws Exception{
        AddProcedureController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////

    /*private void sendProcedureToView(Procedure selectedItem) throws Exception{
        int procedure_id = selectedItem.getProcedureId();

        ResultSet rs = Procedure.queryProcedureInfo(procedure_id);
        rs.next();

        Main.setProcedureFocus((new Procedure(
                rs.getInt("patient_id"),
                rs.getString("procedure_name"),
                rs.getInt("procedure_length"),
                rs.getFloat("procedure_price")
        )));
    }*/



}
