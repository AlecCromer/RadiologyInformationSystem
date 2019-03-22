package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.databaseConnector;

public class User {

    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean validLogin(String email, String password) {
        PreparedStatement ps;
        ResultSet resultSet;
        try {
            ps = databaseConnector.getConnection().prepareStatement("select * from employees where email = ? and first_name = ?"); //First_name is a placeholder for a password column in the Employees table
            ps.setString(1, email);
            ps.setString(2, password);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return true;
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
