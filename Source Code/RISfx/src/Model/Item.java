package Model;

import Controller.databaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Item {

    private int itemID, itemCount;
    private String itemName;
    private float itemCost;


    /**
     * returns all records in the items table of the SQL as a resultset.
     * used in TechEntryController.comboBoxFill()
     * @return
     * @throws Exception
     */
    public static ResultSet queryAllItems() throws Exception{
        return databaseConnector.getConnection().prepareStatement("SELECT * FROM items").executeQuery();
    }

    /**
     * Returns All the item name and cost of all items associated with an appointment, given the appointment ID
     * Used in TechEntryController.getItemList()
     * @param appointment_id
     * @return
     * @throws Exception
     */
    public static ResultSet queryBilling(String appointment_id) throws Exception{
        PreparedStatement addressQuery = databaseConnector.getConnection().prepareStatement(
                "SELECT i.item_name, i.item_cost FROM billing as b, items as i " +
                        "WHERE b.appointment_id=? and i.item_id = b.item_id");
        addressQuery.setInt(1, Integer.parseInt(appointment_id));
        return addressQuery.executeQuery();
    }


    /**
     * Inserts a new charge item associated with a billing ID and appointment ID
     * Used in TechEntryController.addItem()
     * @param appointment_id
     * @param itemID
     * @throws Exception
     */
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

    //Getters setters

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

    public Float getItemCost() {
        return itemCost;
    }
    public void setItemCost(float itemCost) {
        this.itemCost = itemCost;
    }
    public int getItemCount(){
        return itemCount;
    }
    public Float getItemTotal(){
        return this.itemCost*this.itemCount;
    }

    //These are constructors

    /**
     * The compiler says this isn't used, but this is highlighted as if it is. Be wary.
     * @param itemID
     * @param itemName
     * @param itemCost
     */
    public Item(int itemID, String itemName, float itemCost) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCost = itemCost;
    }

    /**
     * Used in TechEntryController.getItemList()
     * @param itemName
     * @param itemCost
     */
    public Item(String itemName, float itemCost) {
        this.itemName = itemName;
        this.itemCost = itemCost;
    }

    /**
     * Used in InvoiceController.getItemList().
     * @param itemName
     * @param itemCost
     * @param itemCount
     */
    public Item(String itemName, float itemCost, int itemCount) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemCount = itemCount;
    }

    /**
     * Used in InvoiceController.getItemList()
     * @param itemID
     * @param itemCount
     * @param itemName
     * @param itemCost
     */
    public Item(int itemID, int itemCount, String itemName, float itemCost) {
        this.itemID = itemID;
        this.itemCount = itemCount;
        this.itemName = itemName;
        this.itemCost = itemCost;
    }
}
