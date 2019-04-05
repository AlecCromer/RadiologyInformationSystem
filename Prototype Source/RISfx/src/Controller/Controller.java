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
import javafx.scene.control.*;

public class Controller {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label exceptionLabel;
    @FXML
    private Button loginButton;

    public void onLoginButtonPushed(ActionEvent event) {
        Employee user = new Employee(passwordTextField.getText(), usernameTextField.getText());

        try {
            if (user.validLogin(usernameTextField.getText(), passwordTextField.getText())) {
                exceptionLabel.setText("Login Successful");
                ResultSet rs = Employee.querySessionEmployee(user.getEmail(), user.getFirstName());
                rs.next();

                Main.setSessionUser(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                ));
                try {
                    Main.getSessionUser().setPermissions();
                }catch (Exception e){
                    System.out.println("No permissions 4 u");
                }
                Main.successfulLogin();
            } else {
                changeScene(0);

            }
        } catch (Exception a) {
            exceptionLabel.setText("Error");
        }
    }

    private void changeScene(int sceneID) {
        switch (sceneID) {
            case 0: {
                Alert rejection = new Alert(Alert.AlertType.ERROR);
                rejection.setTitle("Error");
                rejection.setHeaderText(null);
                rejection.setContentText("Incorrect username or password. Please try again.");
                rejection.showAndWait();
                usernameTextField.clear();
                passwordTextField.clear();
                usernameTextField.setStyle("-fx-border-color: red");
                passwordTextField.setStyle("-fx-border-color: red");
                return;
            }



        }
    }

}
