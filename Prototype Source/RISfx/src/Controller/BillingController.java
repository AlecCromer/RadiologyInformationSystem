package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class BillingController {

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }
    public void setBillView() throws Exception {
        Main.setPopupWindow("BillingViews/sample.fxml");
    }
    public static void setBillingList() throws Exception{
        Main.setCenterPane("BillingViews/BillingList.fxml");
    }
}
