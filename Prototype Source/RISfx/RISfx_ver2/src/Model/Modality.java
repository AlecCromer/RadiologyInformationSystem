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

    public static ResultSet queryAllModality() throws Exception{
        return databaseConnector.getConnection().prepareStatement("SELECT * FROM modality").executeQuery();
    }


    public Modality(int machine_id, String machine_name) {
        this.machine_id = machine_id;
        this.machine_name = machine_name;
    }
}
