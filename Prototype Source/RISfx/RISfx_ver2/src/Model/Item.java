package Model;

import Controller.databaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Item {

    private int itemID;
    private String itemName;
    private float itemCost;


    public static ResultSet queryAllItems() throws Exception{
        return databaseConnector.getConnection().prepareStatement("SELECT * FROM items").executeQuery();
    }

    public static ResultSet queryBilling(String appointment_id) throws Exception{
        PreparedStatement addressQuery = databaseConnector.getConnection().prepareStatement(
                "SELECT i.item_name, i.item_cost FROM billing as b, items as i " +
                        "WHERE b.appointment_id=? and i.item_id = b.item_id");
        addressQuery.setInt(1, Integer.parseInt(appointment_id));
        return addressQuery.executeQuery();
    }


    public static void insertNewItem(int appointment_id, int itemID) throws Exception {
        Connection conn = databaseConnector.getConnection();

            PreparedStatement insertNewItem = conn.prepareStatement(
                    "INSERT INTO billing(billing_id, appointment_id, item_id )" +
                            "VALUES (null,?,?)"
            );


        insertNewItem.setInt(1, appointment_id);
        insertNewItem.setInt(2, itemID);

        insertNewItem.executeUpdate();

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

    public Item(String itemName, int itemCost) {
        this.itemName = itemName;
        this.itemCost = itemCost;
    }
}
