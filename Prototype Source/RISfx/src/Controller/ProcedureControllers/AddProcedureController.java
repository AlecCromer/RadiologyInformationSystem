package Controller.ProcedureControllers;

import Controller.Main;
import Model.Procedure;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
