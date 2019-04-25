package Controller.BillingControllers;

import Controller.Main;
import Model.Appointment;
import Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

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

    /**
     * Intializes the invoice
     * @param url
     * @param arg1
     */
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            updateTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sets Invoice.FXML as a popup window using Main.getPopup().
     * sets height and width of popup
     * @throws Exception
     */
    public static void setView() throws Exception{
        Main.getPopup().setHeight(700);
        Main.getPopup().setWidth(630);
       Main.setPopupWindow("BillingViews/Invoice.fxml");
    }

    /**
     * sets the FXML text stuff at the top from appointment object, gets it from Main.getAppointmentFocus() methods
     * runs getItemList() to get list of Item objects
     * populates table cells row by row in invoice.fxml using this list
     */
    public void updateTable() {
        try {
            invoiceTable.setItems(getItemList());

            //fill text
            nameHolder.setText(Main.getAppointmentFocus().getPatientFullName());
            String[] addr = Main.getAppointmentFocus().getAddressAsArray();
            addressLineHolder.setText(addr[0]);
            stateZipHolder.setText(addr[1] + ", " + addr[3] + ", " + addr[2]);
            invoiceID.setText(String.valueOf(Main.getAppointmentFocus().getAppointmentId()));
            dateOfAppt.setText(Main.getAppointmentFocus().getAppointmentDate().toString());
            totalAmount.setText(String.valueOf(Main.getAppointmentFocus().getBalance()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
        description.setCellValueFactory(new PropertyValueFactory<Item, String>("itemName"));
        quantity.setCellValueFactory(new PropertyValueFactory<Item, Integer>("itemCount"));
        price.setCellValueFactory(new PropertyValueFactory<Item, Float>("itemCost"));
        total.setCellValueFactory(new PropertyValueFactory<Item, Float>("itemTotal"));
    }

    ///////////////////
    //List Generators//
    ///////////////////

    /**
     * gets result set, which is a list of charge items associated with the appointmentID via Appointment.queryBillingList(appointmentID of billing list cell clicked on)
     * Manually adds the cost of the procedure to the returnList
     * then iterates through items result set, building an item object for each one
     * returns returnList containing procedure price and any additional charge items
     * @return
     * @throws Exception
     */
    private ObservableList<Item> getItemList() throws Exception{
        ResultSet items = Appointment.queryBillingList(Main.getAppointmentFocus().getAppointmentId());
        ObservableList<Item> returnList = FXCollections.observableArrayList();
        returnList.add(new Item(
           Main.getAppointmentFocus().getProcedureName(),
            Appointment.queryProcedurePrice(Main.getAppointmentFocus().getProcedureId()),
        1
        ));
        while (items.next()){
            if (items.getInt("item_id") != 0) {
                returnList.add(new Item(
                        items.getInt("item_id"),
                        items.getInt("item_amount"),
                        items.getString("item_name"),
                        items.getFloat("item_cost")
                ));
            }
        }
        return returnList;
    }

    /**
     * Shows a popup to show that the invoice has been submitted
     */
    public void sendInvoice(){
        Appointment appt = Main.getAppointmentFocus();
        appt.setPatientStatus("Billed");
        try {
            Appointment.updatePatientStatus(appt.getAppointmentId(), appt.getPatientStatus());
        }catch (Exception e){
            System.out.println("Oof");
        }
        Alert rejection = new Alert(Alert.AlertType.INFORMATION);
        rejection.setTitle("Invoice Complete");
        rejection.setHeaderText(null);
        rejection.setContentText("Invoice has been sent");
        Main.popup.setAlwaysOnTop(false);
        rejection.showAndWait();
        Main.popup.setAlwaysOnTop(true);
        return;
    }
}
