package Controller.ProcedureControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Procedure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class AddProcedureController {
      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TextField procedurePriceEditText, procedureNameEditText, procedureLengthEditText;
    @FXML Button procedureSubmitButton, procedureCancelButton;
    @FXML Slider procedureLengthSlider;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.popup.setHeight(358.0);
        Main.popup.setWidth(227.0);
        Main.setPopupWindow("ProcedureViews/addProcedure.fxml");
    }

    //////////////////
    //Button Methods//
    //////////////////
    public void submitNewProcedure() throws Exception{
        float price = Float.parseFloat(procedurePriceEditText.getText());
        String procedureName = procedureNameEditText.getText();
        int procedureLength = Integer.parseInt(procedureLengthEditText.getText());

        Procedure.insertNewProcedure(price, procedureName, procedureLength);
        Main.getOuter().setDisable(false);
        Main.popup.close();
    }

    public void cancel() throws Exception{
        Main.getOuter().setDisable(false);
        Main.popup.close();
    }
}
