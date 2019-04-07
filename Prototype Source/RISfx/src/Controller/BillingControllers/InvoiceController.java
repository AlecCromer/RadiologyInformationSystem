package Controller.BillingControllers;

import Controller.Main;
import Model.Appointment;
import Model.Invoice;
import Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadPoolExecutor;

public class InvoiceController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML private Text nameHolder, addressLineHolder, stateZipHolder, invoiceID, dateOfAppt, totalAmount;

    @FXML TableView<Item> invoiceTable;
    @FXML TableColumn<Item, String> description;
    @FXML TableColumn<Item, Integer> quantity;
    @FXML TableColumn<Item, Float> price, total;


    ////////////////
    //Initializers//
    ////////////////
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setView() throws Exception{
        Main.setCenterPane("PatientViews/PatientList.fxml");
    }

    public void updateTable() {
        try {
            invoiceTable.setItems(getItemList());
            //PatientList.setItems(getPatientList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
    }

    ///////////////////
    //List Generators//
    ///////////////////
    private ObservableList<Item> getItemList() throws Exception{
        ResultSet items = Appointment.queryBillingList();
        ObservableList<Item> returnList = FXCollections.observableArrayList();
        returnList.add(new Item(
           Main.getAppointmentFocus().getProcedureName(),
            Appointment.queryProcedurePrice(Main.getAppointmentFocus().getProcedureId())
        ));
        while (items.next()){
            returnList.add(new Item(
                items.getInt("item_id"),
                items.getInt("item_amount"),
                items.getString("item_name"),
                items.getFloat("item_cost")
            ));
        }
        return returnList;
    }

    //////////////////
    //Button Methods//
    //////////////////


    ///////////////////
    //Form Validation//
    ///////////////////
}
