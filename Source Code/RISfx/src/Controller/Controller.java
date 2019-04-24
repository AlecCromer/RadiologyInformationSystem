package Controller;

import java.sql.ResultSet;

import javafx.fxml.FXMLLoader;
import Model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * This one isn't in the labeling scheme, it opens the login popup
 */
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
     * When login button is pushed, creates an employee object with the password and username, called user
     * Then does the validLogin method in Employee on the input data.
     * If the login is successful, it sets the exception label, session user using setSessionUser in Main.
     * Then it tries to set permissions, then it runs Main.successfulLogin() which inflates the RIS client.
     *
     * On failed login it sets the exception text to error.
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
     * Called for a failed login, it creates a new alert object, sets the rejection texts, and clears the FXML fields
     * required for login. It also highlights them in red.
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
