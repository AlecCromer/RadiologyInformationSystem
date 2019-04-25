package Controller.TechControllers;

import Controller.Main;
import Controller.ReportControllers.ReportFormController;
import Model.Images;
import Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.io.InputStream;
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
    @FXML
    TableView<Images> image_list;
    @FXML
    TableColumn<Images, String> taken_date, taken_status;
    @FXML
    TableColumn<Images, Image> taken_image;
    @FXML
    TableColumn<Item, String> item_name, item_cost;

    private Image image;

    ////////////////
    //Initializers//
    ////////////////

    /**
     * Sets the view for the tech entry
     * @throws Exception
     */
    public static void setView() throws Exception{
        Main.setCenterPane("TechViews/TechEntry.fxml");
    }

    @SuppressWarnings("Duplicates")
    /**
     * Initializes the tech entry controller
     */
    public void initialize(URL url, ResourceBundle arg1) {
        updateTable();
        image_list.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    Images photo = image_list.getSelectionModel().getSelectedItem();
                    ReportFormController.setView(Main.getAppointmentFocus().getAppointmentId(), photo.getImage_id(), false);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * Populates of the tables
     */
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
        taken_status.setCellValueFactory(new PropertyValueFactory<Images, String>("status"));
        item_name.setCellValueFactory(new PropertyValueFactory<Item, String>("itemName"));
        item_cost.setCellValueFactory(new PropertyValueFactory<Item, String>("itemCost"));
    }

    public void popupUpdateTable(){
        updateTable();
    }

    /**
     * Changes the view to the add image controller
     * @throws Exception
     */
    public void captureImage() throws Exception{

        AddImageController.setView(appointmentIDField.getText());
    }

    ///////////////////
    //List Generators//
    ///////////////////

    /**
     * Fills the drop down box for items
     * @throws Exception
     */
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

    /**
     * Retrieves the image list and sets up the table
     * @return
     * @throws Exception
     */
    private ObservableList<Images>  getImageList() throws Exception {
        ResultSet rs = Images.queryImageList(String.valueOf(Main.getAppointmentFocus().getAppointmentId()));
        ObservableList<Images>/*<String>*/ images = FXCollections.observableArrayList();

        while (rs.next()){
            InputStream is = rs.getBinaryStream("imagedata");

            image = SwingFXUtils.toFXImage(ImageIO.read(is), null);
            images.add(new Images(image, rs.getString("exam_date"), rs.getString("status"), rs.getString("image_id")));
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

    /**
     * Sets the back page
     * @throws Exception
     */
    public void setBackPage() throws Exception {
        Main.setBackPage();
    }

    /**
     * Adds item to to the billing list
     * @throws Exception
     */
    public void addItem() throws Exception{

        if(ItemBox.getValue() != null){
            //Removes the machine name, leaving only the machine ID
            String[] itemId = ItemBox.getValue().split(": ");
            Item.insertNewItem(Main.getAppointmentFocus().getAppointmentId(), Integer.parseInt(itemId[0]));
        }

        updateTable();

    }

    private void sendImageToReport(Images selectedItem) throws Exception{
        String exam_date = selectedItem.getExam_date();
        Image image = selectedItem.getImagedata();
        String appointment_id = String.valueOf(Main.getAppointmentFocus().getAppointmentId());
        System.out.println(appointment_id);
        // ResultSet rs = Report.gatherPatientInfo(image);
        //rs.next();
        //Main.setAppointmentFocus(Appointment.generateAppointmentFocus(rs));
    }

    ///////////////////
    //Form Validation//
    ///////////////////


}
