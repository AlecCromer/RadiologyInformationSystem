package Model;

import Controller.databaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Employee {



      ////////////////////
     //Database Queries//
    ////////////////////
    public static ResultSet queryEmployeeSchedule(LocalDate scheduleDate, int machineId) throws Exception{
        Connection conn = databaseConnector.getConnection();

        PreparedStatement employeeSchedule = conn.prepareStatement(
                "SELECT  CONCAT(employees.first_name, \" \", employees.last_name) AS employee_name, employees.employee_id, " +
                        "employee_schedule.start_time, employee_schedule.end_time, modality.machine_id, modality.machine_name " +
                        "FROM modality, employee_schedule " +
                        "INNER JOIN employees ON employee_schedule.employee_id=employees.employee_id " +
                        "WHERE " +
                        "employee_schedule.start_time BETWEEN ? and ? && " +
                        "modality.machine_id = ?"
        );
        employeeSchedule.setString(1, scheduleDate.toString()+ " 00:00:00");
        employeeSchedule.setString(2, scheduleDate.toString()+ " 20:00:00");
        employeeSchedule.setInt(3, machineId);
        return employeeSchedule.executeQuery();
    }




    private int employeeId;
    private String firstName, lastName, email;

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public Employee(int employeeId, String firstName, String lastName, String email) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
