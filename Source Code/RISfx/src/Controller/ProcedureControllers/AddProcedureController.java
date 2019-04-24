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

    /**
     * uses main.setPopupWindow to load addProcedure.fxml as a popup. Sets height and width.
     * @throws Exception
     */
    public static void setView() throws Exception{
        Main.popup.setHeight(358.0);
        Main.popup.setWidth(227.0);
        Main.setPopupWindow("ProcedureViews/addProcedure.fxml");
    }

    //////////////////
    //Button Methods//
    //////////////////

    /**
     * Onclick method called by the .fxml buttons. reads the procedure price, name, and length,
     * then uses Procedure.insertNewProcedure to submit the value to the database.
     *
     * main.yadda yaddas close the window.
     * @throws Exception
     */
    public void submitNewProcedure() throws Exception{
        float price = Float.parseFloat(procedurePriceEditText.getText());
        String procedureName = procedureNameEditText.getText();
        int procedureLength = Integer.parseInt(procedureLengthEditText.getText());

        Procedure.insertNewProcedure(price, procedureName, procedureLength);
        Main.getOuter().setEffect(null);
        Main.popup.close();
    }

    /**
     * closes the popup without doing anything.
     * @throws Exception
     */
    public void cancel() throws Exception{
        Main.getOuter().setEffect(null);
        Main.popup.close();
    }
}
