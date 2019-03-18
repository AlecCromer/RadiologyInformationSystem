package Controller.TechControllers;

import Controller.Main;
import Model.Appointment;
import Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class TechEntryController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField pNameField,             appointmentIDField, appointmentDateField,
                    appointmentTimeField,   signInField,        signOutField;
    @FXML
    ComboBox<String> ItemBox;
    @FXML
    TableView<Item> ItemList;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
          Main.setCenterPane("TechViews/TechEntry.fxml");
    }

    @SuppressWarnings("Duplicates")
    public void initialize(URL url, ResourceBundle arg1) {
        pNameField.setText(Main.getAppointmentFocus().getPatientFullName());
        appointmentIDField.setText(String.valueOf(Main.getAppointmentFocus().getAppointmentId()));
        appointmentDateField.setText((new SimpleDateFormat("MM/dd/yyyy")).format(Main.getAppointmentFocus().getAppointmentDate()));
        appointmentTimeField.setText((new SimpleDateFormat("HH:mm")).format(Main.getAppointmentFocus().getAppointmentTime()));
        if (Main.getAppointmentFocus().getPatientSignIn() != null) {
            signInField.setText((new SimpleDateFormat("HH:mm")).format(Main.getAppointmentFocus().getPatientSignIn()));
        }
        if (Main.getAppointmentFocus().getPatientSignOut() != null) {
            signOutField.setText((new SimpleDateFormat("HH:mm")).format(Main.getAppointmentFocus().getPatientSignOut()));
        }

        try {
            comboBoxFill();
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    private void updateTable(){
        try {
            //ItemList.setItems(getItemList());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }
    }

      ///////////////////
     //List Generators//
    ///////////////////
    private void comboBoxFill() throws Exception{
          ResultSet rs = Item.queryAllItems();
          ObservableList<String> items = FXCollections.observableArrayList();

          while(rs.next()){
              items.add(rs.getInt("item_id")+ ": "+ rs.getString("item_name"));
          }

          ItemBox.setItems(items);
      }


      //////////////////
     //Button Methods//
    //////////////////
      public void setBackPage() throws Exception {
          Main.setBackPage();
      }

      ///////////////////
     //Form Validation//
    ///////////////////
}
