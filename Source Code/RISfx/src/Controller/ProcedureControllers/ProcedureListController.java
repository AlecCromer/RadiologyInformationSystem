package Controller.ProcedureControllers;

import Controller.Main;
import Model.Patient;
import Model.Procedure;
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
import Controller.ProcedureControllers.AddProcedureController;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ProcedureListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////

    @FXML private TableView<Procedure>            ProcedureList;
    @FXML private TableColumn<Procedure, String>  procedureID, procedureName;
    @FXML private TableColumn<Procedure, Integer>   procedureLength;
    @FXML private TableColumn<Procedure, Float>     procedureCost;
    @FXML
    private TextField searchField;
      ////////////////
     //Initializers//
    ////////////////
    /**
     * Runs the method updateTable(); to fill the table with data from the database
     * Upon loading the view
     */
    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
        try {
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /**
     *Sets centerPane with the Procedure list view
     */
    public static void setView()throws Exception{
        Main.setCenterPane("ProcedureViews/ProcedureList.fxml");
    }
    /**
     *Fills table with data from the database
     *Search method takes the filled data and filters it and then loads it back into the
     * table to allow the user to sort through the table
     */
    public void updateTable() throws Exception {
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

        FilteredList<Procedure> sortedProcedure = new FilteredList<>(getProcedureList(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortedProcedure.setPredicate(procedure -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searched = newValue.toLowerCase();

                if (procedure.getProcedureName().toLowerCase().contains(searched)){
                    return true;
                }
                else if (Integer.toString(procedure.getProcedureId()).contains(searched)){
                    return true;
                }
                else if (Float.toString(procedure.getPrice()).contains(searched)){
                    return true;
                }
                else if (Integer.toString(procedure.getProcedureLength()).contains(searched)){
                    return true;
                }
                return false;
            });
        });

        SortedList<Procedure> sortedData = new SortedList<>(sortedProcedure);

        sortedData.comparatorProperty().bind(ProcedureList.comparatorProperty());

        ProcedureList.setItems(sortedProcedure);
    }

      ////////////////////
     //Database Queries//
    ////////////////////



      ///////////////////
     //List Generators//
    ///////////////////
    /**
     *Gets data from the database so it can be loaded into the table
     *
     */

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
    /**
     *Button for adding in a new procedure once it is press
     *it will load the view to fill out the information
     */
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
