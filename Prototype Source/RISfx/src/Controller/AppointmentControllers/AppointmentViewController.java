package Controller.AppointmentControllers;

import Controller.Controller;
import Controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentViewController implements Initializable {

    public static void setView() throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentView.fxml");
    }

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }

    public void initialize(URL url, ResourceBundle arg1) {
    }

    public void showAppointmentFiles(ActionEvent actionEvent) throws Exception{
        AppointmentFilesController.setView();
    }
}
