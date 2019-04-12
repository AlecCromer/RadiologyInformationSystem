package Controller.TechControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class ScheduleEmployeeController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML ComboBox<Employee>    EmployeeBox;
    @FXML DatePicker            ScheduleDate;
    @FXML Slider                StartSlide, EndSlide;
    @FXML TextField             StartField, EndField;
    @FXML Button                SubmitButton;


      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.popup.setHeight(430.0);
        Main.popup.setWidth(480.0);
        Main.setPopupWindow("TechViews/ScheduleEmployee.fxml");
    }

    @SuppressWarnings("Duplicates")
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            EmployeeBox.setItems(getEmployeeList());

            StringConverter<Double> stringConverter = new StringConverter<>() {

                @Override
                public String toString(Double object) {

                    return object.intValue() + ":00";
                }

                @Override
                public Double fromString(String string) {
                    return null;
                }
            };
            StartSlide.valueProperty().addListener((observable, oldValue, newValue) ->
                    StartField.setText(stringConverter.toString(newValue.doubleValue())));
            EndSlide.valueProperty().addListener((observable, oldValue, newValue) ->
                    EndField.setText(stringConverter.toString(newValue.doubleValue())));

        }catch (Exception e){e.printStackTrace();}
    }


      ///////////////////
     //List Generators//
    ///////////////////
    private ObservableList<Employee> getEmployeeList() throws Exception {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        try(ResultSet resultSet = Employee.queryAllEmployees()){
            while (resultSet.next()){
                employees.add(new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email")
                ));
            }
        }catch(SQLException ex){
            databaseConnector.displayException(ex);
            System.out.println("Someone didn't set up their DATABASE!!");
            return null;
        }
        return employees;
    }


      //////////////////
     //Button Methods//
    //////////////////
    public void submitNewSchedule() throws Exception{
        String startTime = (StartField.getText().length()<5) ? "0"+StartField.getText() : StartField.getText();
        String endTime = (EndField.getText().length()<5) ? "0"+EndField.getText() : EndField.getText();

        Employee.insertNewSchedule(
                EmployeeBox.getValue().getEmployeeId(),
                ScheduleDate.getValue(),
                LocalTime.parse(startTime),
                LocalTime.parse(endTime));
        Main.getOuter().setEffect(null);
        Main.popup.close();
    }

}
