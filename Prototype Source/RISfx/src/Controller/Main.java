package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private static Stage primaryStage = new Stage();
    //Use a boarderPane to present Unified Menu across all windows
    //Use methods to edit
    private static BorderPane Outer = new BorderPane();
    private static BorderPane RIS_Container = new BorderPane();
    private static ArrayList<Node> backNodeList = new ArrayList<>();
    private static Scene MainScene = new Scene(Outer,1600, 900);
    public static Stage popup = new Stage();

    @Override
    public void start(Stage stage) throws Exception{
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
        PatientController.setPatientList();

        //Finally show initial stage
        primaryStage.show();
    }


    //Called to set the main view
    //sets the right to null in case of expanded view
    //Pushes a node onto our back button list
    public static void setCenterPane(String fxmlName)throws Exception{
        try {
            RIS_Container.setRight(null);
            backNodeList.add(RIS_Container.getCenter());
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

    //Determines what all needs to be done when the popup window closes
    private void closeWindowEvent() {
        Outer.setDisable(false);
    }

    //Getters and Setters
    public static BorderPane getRIS_Container(){
        return RIS_Container;
    }









    //Useless but necessary
    public static void main(String[] args) {
        launch(args);
    }
}
