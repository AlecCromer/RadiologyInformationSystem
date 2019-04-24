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
import Model.Employee;
import animatefx.animation.Pulse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class checks permissions, then loads the RIS tabs for the user
 */
public class MenuController implements Initializable {

    /**
     * This is the permissions checker for the session user that decides what tabs to remove.
     * @param url
     * @param arg1
     */

    public void initialize(URL url, ResourceBundle arg1)  {
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
        if(!(pms.contains(5) || pms.contains(4) || pms.contains(6))){
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
        if (pms.contains(5)){
            try {
                PatientListController.setView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pms.contains(4) && !pms.contains(2)){
            try {
                WorkListController.setView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                PatientListController.setView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This sets the patient list xml to the center pane by executing PatientListController.setView()
     * when you click on the RIS tab that says patient
     * @param actionEvent
     * @throws Exception
     */
    public void setPatientList(ActionEvent actionEvent) throws Exception{
       new Pulse(patientList).play();
        PatientListController.setView();
    }

    /**
     * Sets AppointmentList.fxml to center pane when you click on Appointment List by running
     *         AppointmentListController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setAppointmentList(ActionEvent actionEvent) throws Exception{
        new Pulse(appointmentList).play();
        AppointmentListController.setView();
    }

    /**
     * sets ReferralList.fxml to center pane when you click on referrals by executing
     *         ReferralListController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setReferralView(ActionEvent actionEvent) throws Exception{
        new Pulse(refferals).play();
        ReferralListController.setView();
    }

    /**
     * sets BillingList.fxml to center pane when you click on referrals by executing
     *         BillingListController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setBillingList(ActionEvent actionEvent) throws Exception{
        new Pulse(billing).play();
        BillingListController.setView();
    }

    /**
     * sets worklist.fxml to center pane when you click on worklist by executing
     *         WorkListController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setWorkList(ActionEvent actionEvent) throws Exception{
        new Pulse(workList).play();
        WorkListController.setView();
    }

    /**
     * opens ScheduleEmployee.fxml popup by running
     *         ScheduleEmployeeController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setScheduleEmployee(ActionEvent actionEvent) throws Exception{
        new Pulse(scheduleEmployee).play();
        ScheduleEmployeeController.setView();
    }

    /**
     * opens ProcedureList.fxml by running setview method of procedurelistcontroller
     *         ProcedureListController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setProcedureList(ActionEvent actionEvent) throws Exception{
        new Pulse(manageProcedures).play();
        ProcedureListController.setView();
    }

    /**
     * opens reportlist.fxml by running setview method of reportlistcontroller
     *         ReportListController.setView();
     * @param actionEvent
     * @throws Exception
     */
    public void setReportList(ActionEvent actionEvent) throws Exception{
        new Pulse(reportList).play();
        ReportListController.setView();
    }

    @FXML VBox tabsVBox;
    @FXML Button    patientList, appointmentList, refferals,
                    billing, workList, scheduleEmployee,
                    manageProcedures, reportList;
}
