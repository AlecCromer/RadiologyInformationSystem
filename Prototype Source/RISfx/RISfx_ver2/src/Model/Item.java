package Model;

import Controller.databaseConnector;

import java.sql.ResultSet;

public class Item {

    private int itemID;
    private String itemName;
    private float itemCost;


    public static ResultSet queryAllItems() throws Exception{
        return databaseConnector.getConnection().prepareStatement("SELECT * FROM items").executeQuery();
    }






    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemCost() {
        return itemCost;
    }

    public void setItemCost(float itemCost) {
        this.itemCost = itemCost;
    }


    public Item(int itemID, String itemName, float itemCost) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCost = itemCost;
    }
}
