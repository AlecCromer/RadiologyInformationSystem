package Controller.AppointmentControllers;

import Controller.Controller;
import Controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentListController implements Initializable {

    //sets the View
    public static void setView()throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentList.fxml");
    }

    //Use to initialize the table
    public void initialize(URL url, ResourceBundle arg1) {
        //setSQLQuery("select title, description, content FROM item");
    }



      /////////////////////////
     //Button Function Calls//
    /////////////////////////
    public void setAppointmentView(ActionEvent actionEvent) throws Exception{
        AppointmentViewController.setView();
    }
    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        AddAppointmentController.setView();
    }
}
