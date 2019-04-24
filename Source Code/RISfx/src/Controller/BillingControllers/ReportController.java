package Controller.BillingControllers;

import Controller.Controller;
import Controller.Main;

public class ReportController extends Controller {

    /**
     * Uses setPopupWindow in main to open sample.fxml
     * @throws Exception
     */
    public static void setView() throws Exception{
            Main.setPopupWindow("BillingViews/sample.fxml");
    }
}
