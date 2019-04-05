package Controller.MenuControllers;

import Controller.AppointmentControllers.AppointmentListController;
import Controller.BillingControllers.BillingListController;
import Controller.ProcedureControllers.ProcedureListController;
import Controller.PatientControllers.PatientListController;
import Controller.ReferralControllers.ReferralListController;
import Controller.ReportControllers.ReportListController;
import Controller.TechControllers.ScheduleEmployeeController;
import Controller.TechControllers.WorkListController;
import Controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public void initialize(URL url, ResourceBundle arg1) {
        ArrayList<Integer> pms = Main.getSessionUser().getPermissions();
        pms.contains(1);
    }

    public void setPatientList(ActionEvent actionEvent) throws Exception{
        PatientListController.setView();
    }

    public void setAppointmentList(ActionEvent actionEvent) throws Exception{
        AppointmentListController.setView();
    }

    public void setReferralView(ActionEvent actionEvent) throws Exception{
        ReferralListController.setView();
    }

    public void setBillingList(ActionEvent actionEvent) throws Exception{
        BillingListController.setView();
    }

    public void setWorkList(ActionEvent actionEvent) throws Exception{
        WorkListController.setView();
    }

    public void setScheduleEmployee(ActionEvent actionEvent) throws Exception{
        ScheduleEmployeeController.setView();
    }

    public void setProcedureList(ActionEvent actionEvent) throws Exception{
        ProcedureListController.setView();
    }
    public void setReportList(ActionEvent actionEvent) throws Exception{
        ReportListController.setView();
    }

    @FXML
    VBox tabsVBox;
}
