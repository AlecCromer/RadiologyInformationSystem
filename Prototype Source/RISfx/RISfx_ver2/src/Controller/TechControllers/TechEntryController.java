package Controller.TechControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Appointment;
import Model.Images;
import Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    @FXML
    TableView<Images> image_list;
    @FXML
    TableColumn<Images, String> taken_date;
    @FXML
    TableColumn<Images, Image> taken_image;
    @FXML
    TableColumn<Item, String> item_name, item_cost;

    private Image image;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
          Main.setCenterPane("TechViews/TechEntry.fxml");
    }

    @SuppressWarnings("Duplicates")
    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
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
            ItemList.setItems(getItemList());
            image_list.setItems(getImageList());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("UNABLE TO FILL TABLE");
            e.printStackTrace();
        }

        taken_image.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(200);
            imageview.setFitWidth(200);

            //Set up the Table
            TableCell<Images, Image> cell = new TableCell<Images, Image>() {
                public void updateItem(Image item, boolean empty) {
                    if (item != null) {
                        imageview.setImage(item);
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(imageview);
            return cell;
        });
        taken_image.setCellValueFactory(new PropertyValueFactory<Images, Image>("imagedata"));
        taken_date.setCellValueFactory(new PropertyValueFactory<Images, String>("Exam_date"));
        item_name.setCellValueFactory(new PropertyValueFactory<Item, String>("itemName"));
        item_cost.setCellValueFactory(new PropertyValueFactory<Item, String>("itemCost"));
    }

    public void popupUpdateTable(){
        updateTable();
    }

    public void captureImage() throws Exception{

        AddImageController.setView(appointmentIDField.getText());
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

    ///////////////////
    //List Generators//
    ///////////////////
    private ObservableList<Images>  getImageList() throws Exception {
        ResultSet rs = Images.queryImageList(String.valueOf(Main.getAppointmentFocus().getAppointmentId()));
        ObservableList<Images>/*<String>*/ images = FXCollections.observableArrayList();

            while (rs.next()){
                InputStream is = rs.getBinaryStream("imagedata");

                image = SwingFXUtils.toFXImage(ImageIO.read(is), null);
                images.add(new Images(image, rs.getString("exam_date")));
            }
        return images;
    }
    private ObservableList<Item> getItemList() throws Exception {
        ResultSet rs = Item.queryBilling(String.valueOf(Main.getAppointmentFocus().getAppointmentId()));
        ObservableList<Item>/*<String>*/ item = FXCollections.observableArrayList();

        while (rs.next()){

            item.add(new Item(rs.getString("item_name"), rs.getInt("item_cost")));
        }
        return item;
    }

      //////////////////
     //Button Methods//
    //////////////////
      public void setBackPage() throws Exception {
          Main.setBackPage();
      }

      public void addItem() throws Exception{

        if(ItemBox.getValue() != null){
            //Removes the machine name, leaving only the machine ID
            String[] itemId = ItemBox.getValue().split(": ");
            Item.insertNewItem(Main.getAppointmentFocus().getAppointmentId(), Integer.parseInt(itemId[0]));
        }

        updateTable();

      }

      ///////////////////
     //Form Validation//
    ///////////////////


}
