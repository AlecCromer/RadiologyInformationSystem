package Controller.TechControllers;

import Controller.Main;
import Model.Images;
import Model.Modality;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.annotation.Repeatable;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AddImageController  implements Initializable {


    @FXML
    ImageView imageview;

    @FXML
    Button submit_image, uploadImage,select;

    @FXML
    ComboBox<String> machineBox;

    private static String director = "", filename = "", filePath = "", file="";
    static String Appointment_id ="";


    public void initialize(URL url, ResourceBundle arg1){
        uploadImage.setVisible(false);
        try {
            comboBoxFill();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setView(String appointment_id)throws Exception{
        Appointment_id = appointment_id;
        Main.popup.setWidth(620);
        Main.popup.setHeight(600);
        Main.setPopupWindow("TechViews/addImage.fxml");
    }
    // User presses the select Image button and it gets displayed on screen
    public void captureImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
        File choosenfile = fileChooser.showOpenDialog(Main.getPopup());
        Main.popup.setAlwaysOnTop(false);
        file = choosenfile.getPath();
        Image image = new Image("file:" + choosenfile);
        imageview.setImage(image);
        uploadImage.setVisible(true);
    }

    //////////////////
    //Button Methods//
    //////////////////
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

    ///////////////////
    //List Generators//
    ///////////////////
    private void comboBoxFill() throws Exception{
        ResultSet rs = Modality.queryAllModality();
        ObservableList<String> machine = FXCollections.observableArrayList();

        while(rs.next()){
            machine.add(rs.getInt("machine_id")+ ": "+ rs.getString("machine_name"));
        }
        System.out.println("hello?");
        machine.toString();
        machineBox.setItems(machine);
    }

}