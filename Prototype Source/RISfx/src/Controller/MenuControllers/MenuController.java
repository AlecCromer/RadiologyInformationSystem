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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public void initialize(URL url, ResourceBundle arg1) {
        //Remove all buttons they don't have permission for
        ArrayList<Integer> pms = Main.getSessionUser().getPermissions();
        //Check for Patient List
        if(!(pms.contains(1) || pms.contains(2) || pms.contains(3) ||
           pms.contains(5) || pms.contains(6))){
            tabsVBox.getChildren().remove(patientList);
        }
        //Check for Appointment List
        if(!(pms.contains(3) || pms.contains(5)|| pms.contains(6))){
            tabsVBox.getChildren().remove(appointmentList);
        }
        //Check for Referral List
        if(!(pms.contains(3) || pms.contains(5)|| pms.contains(6))){
            tabsVBox.getChildren().remove(refferals);
        }
        //Check for Billing
        if(!(pms.contains(3) || pms.contains(5)|| pms.contains(6))){
            tabsVBox.getChildren().remove(billing);
        }
        //Check for Work List
        if (!(pms.contains(4) || pms.contains(2)|| pms.contains(6))){
            tabsVBox.getChildren().remove(workList);
        }
        //Check for Schedule Employee
        if(!(pms.contains(5) || pms.contains(6))){
            tabsVBox.getChildren().remove(scheduleEmployee);
        }
        //Check for Manage Procedures
        if(!(pms.contains(6))){
            tabsVBox.getChildren().remove(manageProcedures);
        }
        //Check for Report List
        if(!(pms.contains(1) || pms.contains(2) || pms.contains(6))){
            tabsVBox.getChildren().remove(reportList);
        }
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

    @FXML VBox tabsVBox;
    @FXML Button    patientList, appointmentList, refferals,
                    billing, workList, scheduleEmployee,
                    manageProcedures, reportList;
}
