package Controller;

import javafx.event.ActionEvent;

public class MenuController {


    public void setPatientList(ActionEvent actionEvent) throws Exception{
        PatientController.setPatientList();
    }

    public void setAppointmentList(ActionEvent actionEvent) throws Exception{
        AppointmentController.setAppointmentList();
    }

    public void setReferralView(ActionEvent actionEvent) throws Exception{
        ReferralController.setReferralView();
    }

    public void setBillingList(ActionEvent actionEvent) throws Exception{
        BillingController.setBillingList();
    }

    public void setWorkList(ActionEvent actionEvent) throws Exception{
        TechController.setWorkList();
    }
}
