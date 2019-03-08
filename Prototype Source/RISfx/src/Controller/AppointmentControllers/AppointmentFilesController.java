package Controller.AppointmentControllers;

import Controller.Controller;
import Controller.Main;
import javafx.fxml.FXMLLoader;

public class AppointmentFilesController extends Controller {
    public static void setView() throws Exception{
        Main.getRIS_Container().setRight(FXMLLoader.load(ReportFormController.class.getResource("/View/PatientViews/ReportForm.fxml")));
    }
}
