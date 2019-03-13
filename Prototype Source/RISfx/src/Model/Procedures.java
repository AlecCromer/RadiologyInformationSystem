package Model;

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
}
