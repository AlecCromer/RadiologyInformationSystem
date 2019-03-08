package Controller.ReferralControllers;

import Controller.Main;
import javafx.event.ActionEvent;

public class ReferralListController {

    public static void setReferralView() throws Exception{
        Main.setCenterPane("ReferralViews/ReferralList.fxml");
    }

    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        Main.setPopupWindow("ReferralViews/addReferredPatient.fxml");
        Main.popup.setResizable(false);
        Main.popup.setHeight(550);
        Main.popup.setWidth(520);
    }

    public void setReferralForm(ActionEvent actionEvent) throws Exception{
        ReferralFormController.setView();
    }
}
