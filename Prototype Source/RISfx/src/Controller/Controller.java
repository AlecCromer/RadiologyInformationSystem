package Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.databaseConnector;
import Model.Employee;
import Model.User;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label exceptionLabel;
    @FXML private Button loginButton;

    public void onLoginButtonPushed(ActionEvent event) {
        Employee user = new Employee(passwordTextField.getText(),usernameTextField.getText());
        try{
            if(user.validLogin(usernameTextField.getText(), passwordTextField.getText())) {
                exceptionLabel.setText("Login Successful");
                ResultSet rs = Employee.querySessionEmployee(user.getEmail(), user.getFirstName());
                rs.next();

                Main.setSessionUser(new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
                ));

                Main.successfulLogin();
            }
            else {
                exceptionLabel.setText("Incorrect user name or password");
            }
        }
        catch(Exception a) {
            exceptionLabel.setText("Error");
        }
    }

}
