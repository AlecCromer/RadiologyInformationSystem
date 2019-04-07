package Controller.TechControllers;

import Controller.Main;
import Model.Images;
import Model.Modality;
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

import java.awt.*;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AddImageController  implements Initializable {


    @FXML
    ImageView imageview;

    @FXML
    Button submit_image, uploadImage;

    @FXML
    ComboBox<String> machineBox;

    private String director = "", filename = "", filePath = "";
    static String Appointment_id ="";


    public void initialize(URL url, ResourceBundle arg1){
        uploadImage.setVisible(false);

        try {
            comboBoxFill();
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    public static void setView(String appointment_id)throws Exception{

        Appointment_id = appointment_id;
        Main.popup.setWidth(620);
        Main.popup.setHeight(600);
        Main.setPopupWindow("TechViews/addImage.fxml");
    }
    // User presses the select Image button and it gets displayed on screen
    public void captureImage(ActionEvent event){
        Main.popup.setAlwaysOnTop(false);
        String file = fileChooser();
        Image image = new Image("file:" + file);
        imageview.setImage(image);
        uploadImage.setVisible(true);
    }

    // popup for user to select photo

    public String fileChooser() {
        Frame JFrame = new Frame();
        FileDialog fd = new FileDialog(JFrame, "Choose a file", FileDialog.LOAD);
        fd.setAlwaysOnTop(true);

        try{
        fd.setDirectory("C:\\");
        fd.setFile("*.jpg;*.jpeg;*.png");
        fd.setVisible(true);
        filename = fd.getFile();
        director = fd.getDirectory().replace('\\', '/');
        System.out.println(director + fd.getFile());
        }catch (Exception e){
            e.printStackTrace();
        }
        if (filename == null) {
            System.out.println("User Cancelled the choice");
        } else {
            System.out.println("User chose " + filename);
            filePath = director + fd.getFile();
        }
        JFrame.dispatchEvent(new WindowEvent(JFrame, WindowEvent.WINDOW_CLOSING));
        return director + fd.getFile();

    }



    //////////////////
    //Button Methods//
    //////////////////
    public void submitNewPhoto() throws Exception{

        if (!filePath.isEmpty() && machineBox.getValue() != null){

            //Removes the machine name, leaving only the machine ID
            String[] machine_id = machineBox.getValue().split(": ");

           Images.insertNewImage( filePath, Integer.parseInt(machine_id[0]), Appointment_id);
            Main.popup.close();
            Main.getOuter().setDisable(false);
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