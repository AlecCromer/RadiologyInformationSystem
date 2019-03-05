package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class AppointmentController {


    //All Appointment Window Setters
    public void setBackPage()throws Exception{
        Main.setBackPage();
    }

    public static void setAppointmentList()throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentList.fxml");
    }

    public static void setAppointmentView()throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentView.fxml");
    }
    public void setAppointmentView(ActionEvent actionEvent) throws Exception{
        Main.setCenterPane("AppointmentViews/AppointmentView.fxml");
    }

    public static void setAddAppointment()throws Exception{
        Main.setPopupWindow("AppointmentViews/addAppointment.fxml");
    }
    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        Main.setPopupWindow("AppointmentViews/addAppointment.fxml");
    }

    public void showFiles() throws Exception{
        Main.getRIS_Container().setRight(FXMLLoader.load(getClass().getResource("/View/PatientViews/FilesViewer.fxml")));
    }


}
