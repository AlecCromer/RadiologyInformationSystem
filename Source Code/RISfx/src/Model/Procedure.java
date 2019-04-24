package Model;

import Controller.databaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Procedure {
    /**
     * Returns the list for procedure combo boxes.
     * @return
     * @throws Exception
     */
    public static ObservableList<String> getProcedureList() throws Exception{
        ObservableList<String> rtn = FXCollections.observableArrayList();

        Connection conn = databaseConnector.getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM `procedures`").executeQuery();
        while (rs.next()){
            rtn.add(
                    rs.getInt("procedure_id") + ": " + rs.getString("procedure_name")
            );
        }

        return rtn;
    }



    ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private float price;
    private int procedureId, procedureLength;
    private String procedureName;


      ////////////////////
     //Database Queries//
    ////////////////////

    /**
     * Returns a resultset containing all information in SQL procedure table
     * Used in procedureListController.getProcedureList()
     * @return
     * @throws Exception
     */
    public static ResultSet queryAllProcedures() throws Exception{
        ResultSet resultSet = databaseConnector.getConnection().prepareStatement(
                "select * FROM procedures"
        ) .executeQuery();

        return resultSet;
    }

    /**
     * Gets the length of a procedure given a procedure's ID.
     * Used in AddAppointmentController.generateTimeSlots()
     * @param procedureId
     * @return
     * @throws Exception
     */
    public static int queryProcedureLength(int procedureId)throws Exception{
        ResultSet procL = databaseConnector.getConnection().prepareStatement(
                "SELECT procedure_length " +
                        "FROM procedures " +
                        "WHERE procedure_id = " + procedureId).executeQuery();
        procL.next();
        return procL.getInt("procedure_length");
    }

    /**
     * Inserts a new procedure's price, name, and length into a the SQL database.
     * Used in AddProcedureController.submitNewProcedure();
     * @param price
     * @param procedureName
     * @param procedureLength
     * @throws Exception
     */
    public static void insertNewProcedure(float price, String procedureName, int procedureLength) throws Exception {
        PreparedStatement st = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO `procedures` (`procedure_length`, `procedure_name`, `procedure_price`) " +
                        "VALUES (" + procedureLength +", " + '"' + procedureName + '"' + ", " + price + " );"
        );
        st.execute();
    }

    /**
     * Returns all information from procedures table where the procedure ID of the record equals the given procedure ID
     * Used in Appointment.setBalance();
     * @param procedure_id
     * @return
     * @throws Exception
     */
    public static ResultSet queryProcedureInfo(int procedure_id) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT * FROM procedures " +
                        "WHERE procedure_id = " + procedure_id).executeQuery();
    }

    ///////////////////
     //Getters/Setters//
    ///////////////////

    public int getProcedureId() {
        return procedureId;
    }
    public void setProcedureId(int procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcedureName() {
        return procedureName;
    }
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public int getProcedureLength() {
        return procedureLength;
    }
    public void setProcedureLength(int procedureLength) {
        this.procedureLength = procedureLength;
    }

    public float getPrice() { return price; }

    public void setPrice(float price) { this.price = price; }

    public Procedure(int procedure_id, String procedure_name, int procedure_length, float procedure_price){
        this.procedureId    = procedure_id;
        this.procedureName  = procedure_name;
        this.procedureLength = procedure_length;
        this.price = procedure_price;
    }
    public Procedure(int procedureId, String procedureName){
        this.procedureId    = procedureId;
        this.procedureName  = procedureName;
    }
    public Procedure(int procedureId, int procedureLength, String procedureName) {
        this.procedureId = procedureId;
        this.procedureLength = procedureLength;
        this.procedureName = procedureName;
    }
    public Procedure(int procedureId, int procedureLength, String procedureName, Float procedureCost) {
        this.procedureId = procedureId;
        this.procedureLength = procedureLength;
        this.procedureName = procedureName;
        this.price = procedureCost;
    }





}

