package Controller.TechControllers;

import Controller.Main;
import Controller.databaseConnector;
import Model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ResourceBundle;

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
    /**
     * Opens ScheduleEmployee,fxml using the setPopupWindow() in main
     * sets height and width of window.
     * @throws Exception
     */
    public static void setView() throws Exception{
        Main.popup.setHeight(430.0);
        Main.popup.setWidth(480.0);
        Main.setPopupWindow("TechViews/ScheduleEmployee.fxml");
    }

    @SuppressWarnings("Duplicates")
    /**
     * Initializes the schedule employee controller
     */
    public void initialize(URL url, ResourceBundle arg1) {
        try {
            EmployeeBox.setItems(getEmployeeList());

            StringConverter<Double> stringConverter = new StringConverter<>() {

                @Override
                public String toString(Double aDouble) {
                    return aDouble.intValue() + ":00";
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
    /**
     * This generates a list of employee objects. It calls Employees.queryAllEmployees() to return a resultSet, then it
     * iterates through the resultSet using the employee constructor from the Employee class building employee objects.
     * These get added to this list and returned as an observable list.
     * @return
     * @throws Exception
     */
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
    /**
     * This method gets the start time and end time from the fxml boxes where the slider bar time displays.
     * Then it tries to use Employee.insertNewSchedule() to add a new schedule. The catch statement highlights a field
     * in red if it's wrong.
     */
    public void submitNewSchedule(){
        String startTime = (StartField.getText().length()<5) ? "0"+StartField.getText() : StartField.getText();
        String endTime = (EndField.getText().length()<5) ? "0"+EndField.getText() : EndField.getText();

        try {
            Employee.insertNewSchedule(
                    EmployeeBox.getValue().getEmployeeId(),
                    ScheduleDate.getValue(),
                    LocalTime.parse(startTime),
                    LocalTime.parse(endTime));
            Main.getOuter().setEffect(null);
            Main.popup.close();
        }
        catch (Exception e){
            if(EmployeeBox.getValue() == null){
                EmployeeBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if(ScheduleDate.getValue() == null){
                ScheduleDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if(StartSlide.getValue() == 0.0){
                StartField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if(EndSlide.getValue() == 0.0){
                EndField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
        }
    }

}
