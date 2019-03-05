package Controller;

import javafx.event.ActionEvent;

public class ReferralController {

    public static void setReferralView() throws Exception{
        Main.setCenterPane("ReferralViews/ReferralList.fxml");
    }

    public void setAddAppointment(ActionEvent actionEvent) throws Exception{
        Main.setPopupWindow("ReferralViews/addReferredPatient.fxml");
        Main.popup.setResizable(false);
        Main.popup.setHeight(550);
        Main.popup.setWidth(520);
    }
}
