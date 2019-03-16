package Model;

import Controller.databaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Struct;
import java.util.ArrayList;

public class Procedures {

    private int procedureId;
    private String procedureName;

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

    public Procedures(){
        this.procedureId    = -1;
        this.procedureName  = null;
    }
    public Procedures(int procedureId, String procedureName){
        this.procedureId    = procedureId;
        this.procedureName  = procedureName;
    }


    //Returns list for procedure combo boxes
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

    public static int queryProcedureLength(int procedureId)throws Exception{
        ResultSet procL = databaseConnector.getStartConnection().prepareStatement(
                "SELECT procedure_length " +
                        "FROM procedures " +
                        "WHERE procedure_id = " + procedureId).executeQuery();
        procL.next();
        return procL.getInt("procedure_length");
    }
}
