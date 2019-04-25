package Controller;

import java.sql.ResultSet;

import javafx.fxml.FXMLLoader;
import Model.Employee;
import javafx.event.ActionEvent;
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


    /**
     * Sets login view as a popup in a weird way. Sets height, width, makes sure it's not resizeable and can't be
     * maximized. sets left and top border pane to null
     * @throws Exception
     */
    public static void setView() throws Exception{
        Main.getOuter().setLeft(null);
        Main.getOuter().setTop(null);
        Main.getOuter().setCenter(FXMLLoader.load(Controller.class.getResource("../View/LoginView.fxml")));
        Main.getPrimaryStage().setMaxHeight(350);
        Main.getPrimaryStage().setMaxWidth(325);
        Main.getPrimaryStage().setResizable(false);
        Main.getPrimaryStage().setMaximized(false);

    }

    /**
     * The button method when a user presses the login button
     * @param event
     */
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
                    System.out.println("");
                }
                Main.successfulLogin();

            } else {
                changeScene(0);

            }
        } catch (Exception a) {
            exceptionLabel.setText("Error");
        }
    }

    /**
     * Change the scene after logging in
     * @param sceneID
     */
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
