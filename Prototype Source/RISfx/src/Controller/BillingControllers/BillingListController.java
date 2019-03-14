package Controller.BillingControllers;

import Controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BillingListController implements Initializable {

    public static void setView() throws Exception{
        Main.setCenterPane("BillingViews/BillingList.fxml");
    }

    public void initialize(URL url, ResourceBundle arg1){
        //TODO: Fill Table
    }

    public void setBillView(ActionEvent actionEvent) {
    }
}
