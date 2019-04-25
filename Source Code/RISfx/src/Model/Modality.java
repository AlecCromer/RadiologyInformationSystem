package Model;

import Controller.databaseConnector;

import java.sql.ResultSet;

public class Modality {

    private int machine_id;
    private int type;
    private String machine_name;
    private String name, summary;

    public int getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(int machine_id) {
        this.machine_id = machine_id;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }



    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Retrieves all of the machines
     * @return
     * @throws Exception
     */
    public static ResultSet queryAllModality() throws Exception{
        return databaseConnector.getConnection().prepareStatement("SELECT * FROM modality").executeQuery();
    }

    /**
     * Retrieves the machine information
     * @param procedure_id
     * @return
     * @throws Exception
     */
    public static int queryMachineIdByProcedureType(int procedure_id) throws Exception{
        ResultSet rs = databaseConnector.getConnection().prepareStatement(
                "SELECT machine_id FROM procedures WHERE procedure_id = " +procedure_id
        ).executeQuery();
        if(rs.next()){
            return rs.getInt("machine_id");
        }else
            System.out.println("big oof");
        return -1;
    }


    public Modality(int machine_id, String machine_name) {
        this.machine_id = machine_id;
        this.machine_name = machine_name;
    }
}
