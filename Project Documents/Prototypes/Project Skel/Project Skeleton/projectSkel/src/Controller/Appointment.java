/*
package Controller;
import Controller.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Appointment {
    @FXML
    private Label lblText;

    public void handleButton() {
        lblText.setText("");
    }
}

*/

package Controller;

import javafx.beans.property.SimpleStringProperty;
import Controller.AddAppointment;
public class Appointment {
   private final SimpleStringProperty firstName = new SimpleStringProperty("");
   private final SimpleStringProperty lastName = new SimpleStringProperty("");
   private final SimpleStringProperty email = new SimpleStringProperty("");

public Appointment() {
        this("", "", "");
    }

    public Appointment(String firstName, String lastName, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String fName) {
        lastName.set(fName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String fName) {
        email.set(fName);
    }
}
