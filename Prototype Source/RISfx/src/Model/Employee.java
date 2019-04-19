package Model;

import Controller.databaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Employee {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private int employeeId;
    private String firstName, lastName, email;
    private ArrayList<Integer> permissions = new ArrayList<>();


      ////////////////////
     //Database Queries//
    ////////////////////
    /**
     * Database query for all employees available on a date for a specified modality
     * @param scheduleDate The date being queried for
     * @param machineId The ID of the modality in question
     * @return ResultSet of database query containing all technicians available for specified date
     * @throws Exception
     */
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

    /**
     * Database query used to check an email and password against the database
     * @param email Employee's email address
     * @param password Employee's password
     * @return ResultSet of all Employees matching the email/password combination
     * @throws Exception
     */
    public static ResultSet querySessionEmployee(String email, String password) throws Exception{
        PreparedStatement ps = databaseConnector.getConnection().prepareStatement(
            //First_name is a placeholder for a password column in the Employees table
            "SELECT * fROM employees WHERE email = ? AND password = ?"
        );
        ps.setString(1, email);
        ps.setString(2, password);
        return ps.executeQuery();
    }

    /**
     * Database query to check the permissions of this Employee's permissions
     * @return ResultSet containing all user rols associated with this employee
     * @throws Exception
     */
    public ResultSet queryEmployeePermissions() throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT role_id " +
                        "FROM role_relationship " +
                        "WHERE employee_id = " + this.employeeId
        ).executeQuery();
    }

    /**
     * Checks to see if there is at least one employee with the email and password combination specified
     * @param email Employee's email address
     * @param password Employee's password
     * @return True if there is an employee with the correct combination
     * @throws Exception
     */
    public boolean validLogin(String email, String password) throws Exception{
        ResultSet resultSet = Employee.querySessionEmployee(email, password);
        if(resultSet.next()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Database query for all employees
     * @return ResultSet containing every employee in the database
     * @throws Exception
     */
    public static ResultSet queryAllEmployees()throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT * FROM employees"
        ).executeQuery();
    }

    /**
     * Database query to insert a new entry in the schedule table
     * @param employeeId ID of the employee being scheduled
     * @param date Date employee is being scheduled on
     * @param start The starting time of the shift
     * @param end The ending time of the shift
     * @throws Exception
     */
    public static void insertNewSchedule(int employeeId, LocalDate date, LocalTime start, LocalTime end) throws Exception{
        PreparedStatement st = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `employee_schedule` (`schedule_id`, `employee_id`, `start_time`, `end_time`) " +
                        "VALUES (NULL, ?, ?, ?);"
        );
        st.setInt(1, employeeId);
        st.setString(2, date.toString()+ " " + start.toString());
        st.setString(3, date.toString()+ " " + end.toString());
        st.execute();
    }


      ///////////////////
     //Getters/Setters//
    ///////////////////
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

    public String getFullName(){ return firstName + " " + lastName; }

    public ArrayList<Integer> getPermissions() {
        return permissions;
    }

    /**
     * Queries the database and sets an employee's permissions accordingly
     * @throws Exception
     */
    public void setPermissions() throws Exception{
        ResultSet pms = queryEmployeePermissions();
        while(pms.next()){
            this.permissions.add(pms.getInt("role_id"));
        }
    }

    public Employee(){}

      ////////////////
     //Constructors//
    ////////////////
    /**
     * Used to establish and Employee(object)
     * @param employeeId The ID of the employee
     * @param firstName The employee's first name
     * @param lastName The employee's last name
     * @param email The employee's email address
     */
    public Employee(int employeeId, String firstName, String lastName, String email) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Used to establish and Employee(object)
     * @param firstName The employee's first name
     * @param email The employee's email address
     */
    public Employee(String firstName, String email) {
        this.firstName = firstName;
        this.email = email;
    }



      /////////////
     //Overrides//
    /////////////
    /**
     * Overrides the toString method for this class to return the employee's First and Last names
     * @return This Employee's first name and last name
     */
    @Override
    public String toString(){
        return this.firstName + " " + this.lastName;
    }
}
