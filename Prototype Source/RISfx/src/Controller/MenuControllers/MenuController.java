package Controller.MenuControllers;

import Controller.AppointmentControllers.AppointmentListController;
import Controller.BillingControllers.BillingListController;
import Controller.Controller;
import Controller.PatientControllers.PatientListController;
import Controller.ReferralControllers.ReferralListController;
import Controller.TechControllers.TechController;
import javafx.event.ActionEvent;

public class MenuController extends Controller {


    public void setPatientList(ActionEvent actionEvent) throws Exception{
        PatientListController.setPatientList();
    }

    public void setAppointmentList(ActionEvent actionEvent) throws Exception{
        AppointmentListController.setView();
    }

    public void setReferralView(ActionEvent actionEvent) throws Exception{
        ReferralListController.setReferralView();
    }

    public void setBillingList(ActionEvent actionEvent) throws Exception{
        BillingListController.setView();
    }

    public void setWorkList(ActionEvent actionEvent) throws Exception{
        TechController.setWorkList();
    }
}
