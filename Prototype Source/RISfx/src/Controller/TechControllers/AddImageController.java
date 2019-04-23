package Controller.TechControllers;

import Controller.Main;
import Model.Images;
import Model.Modality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.annotation.Repeatable;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * AddImageController is used by a technician to upload an image to the database
 */
public class AddImageController  implements Initializable {


    @FXML
    ImageView imageview;

    @FXML
    Button submit_image, uploadImage,select;

    @FXML
    ComboBox<String> machineBox;

    private static String director = "", filename = "", filePath = "", file="";
    static String Appointment_id ="";

    /**
     * Initializes the add image controller and fills the dropdown box
     * @param url the location for relative paths
     * @param arg1 the resources used to localize the root object
     */
    public void initialize(URL url, ResourceBundle arg1){
        uploadImage.setVisible(false);
        try {
            comboBoxFill();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Used to set the view of the popup window to the add image view
     * @param appointment_id for the appointment id of the image
     * @throws Exception if changing the popup window fails
     */
    public static void setView(String appointment_id)throws Exception{
        Appointment_id = appointment_id;
        Main.popup.setWidth(620);
        Main.popup.setHeight(600);
        Main.setPopupWindow("TechViews/addImage.fxml");
    }

    /**
     *  User presses the select Image button and allows them to select an image
     * @param event event parameter
     */
    public void captureImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
        File chosenfile = fileChooser.showOpenDialog(Main.getPopup());
        Main.popup.setAlwaysOnTop(false);
        try{
        file = chosenfile.getPath();
            if (file != null){
                Image image = new Image("file:" + chosenfile);
                imageview.setImage(image);
                uploadImage.setVisible(true);
            }
        }catch(Exception e){}

    }
    /**
     * Submits the photo into the database
     * @throws Exception if inserting the image into the database or setting view fails
     */
    public void submitNewPhoto() throws Exception{

        if (!file.isEmpty() && machineBox.getValue() != null){

            //Removes the machine name, leaving only the machine ID
            String[] machine_id = machineBox.getValue().split(": ");
            Images.insertNewImage( file, Integer.parseInt(machine_id[0]), Appointment_id);
            Main.popup.close();
            Main.getOuter().setEffect(null);
            Main.getRIS_Container().setCenter(Main.getRIS_Container().getCenter());
            TechEntryController.setView();
        }
        else{
            //TODO SOME SORT OF POPUP LETTING THE USER KNOW THEY FORGOT TO SELECT AN ITEM

        }
    }

    /**
     * Used to create a dropdown of the different modalities
     * @throws Exception When the SQL returns an error
     */
    private void comboBoxFill() throws Exception{
        ResultSet rs = Modality.queryAllModality();
        ObservableList<String> machine = FXCollections.observableArrayList();

        while(rs.next()){
            machine.add(rs.getInt("machine_id")+ ": "+ rs.getString("machine_name"));
        }
        machine.toString();
        machineBox.setItems(machine);
    }

}
