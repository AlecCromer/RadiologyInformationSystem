package Controller;

import Controller.PatientControllers.PatientListController;
import Model.Appointment;
import Model.Patient;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Main extends Application {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private static Stage primaryStage           = new Stage();
    private static BorderPane Outer             = new BorderPane();
    private static BorderPane RIS_Container     = new BorderPane();
    private static ArrayList<Node> backNodeList = new ArrayList<>();
    private static Scene MainScene              = new Scene(Outer,1600, 900);
    private static Patient patientFocus         = new Patient();
    private static Appointment appointmentFocus = new Appointment();
    public static  Stage popup                  = new Stage();


      ////////////////
     //View Setters//
    ////////////////

    //Called to set the main view
    //sets the right to null in case of expanded view
    //Pushes a node onto our back button list
    public static void setCenterPane(String fxmlName)throws Exception{
        try {
            backNodeList.add(RIS_Container.getCenter());
            RIS_Container.setRight(null);
            RIS_Container.setCenter(FXMLLoader.load(Main.class.getResource("../View/"+fxmlName)));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Sets the main view to the top of our back button list
    //Pops the top of the back button list
    public static void setBackPage(){
        RIS_Container.setRight(null);
        RIS_Container.setCenter(backNodeList.get((backNodeList.size()-1)));
        backNodeList.remove(backNodeList.get((backNodeList.size()-1)));
    }

    //Create Popup Window using submitted fxml
    public static void setPopupWindow(String fxmlName) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getResource("../View/" + fxmlName));
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.setAlwaysOnTop(true);
        popup.show();
        Outer.setDisable(true);
    }

    //Determines what all needs to be done when the popup window closes via 'X' button
    private void closeWindowEvent() {
        Outer.setDisable(false);
    }


      ///////////////////////////
     //Variable Getter/Setters//
    ///////////////////////////
    public static BorderPane getRIS_Container(){
          return RIS_Container;
      }
    public static BorderPane getOuter(){return Outer;}

    //Focus variables used to pass data between the views
    public static Patient getPatientFocus() {
        return patientFocus;
    }
    public static void setPatientFocus(Patient patientFocus) {
        Main.patientFocus = patientFocus;
    }

    public static Appointment getAppointmentFocus(){ return appointmentFocus; }
    public static void setAppointmentFocus(Appointment appointmentFocus){
        Main.appointmentFocus = appointmentFocus;
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

    //TODO: (Depricated) Delete at end
    public static ArrayList<Node> getBackNodeList(){
        return backNodeList;
    }


      ////////////////
     //Start Method//
    ////////////////
    @Override
    public void start(Stage stage) throws Exception{
        databaseConnector.getStartConnection();
        primaryStage.setTitle("RIS Clinic System");
        primaryStage.setScene(MainScene);
        //Set the TOP of the borderPane to our menu
        Outer.setTop(FXMLLoader.load(Main.class.getResource("../View/MenuViews/RIS_Menu.fxml")));
        RIS_Container.setTop(FXMLLoader.load(Main.class.getResource("../View/MenuViews/RIS_Tabs.fxml")));
        Outer.setCenter(RIS_Container);

        //Setup a listener for when the popup window is closed
        popup.setOnCloseRequest(event -> {
            this.closeWindowEvent();
        });

        //Set the initial start to our PatientList
        PatientListController.setView();

        //Finally show initial stage
        primaryStage.show();
    }

    //Useless but necessary
    public static void main(String[] args) {
        launch(args);
    }
}
