package Controller;

import Model.Appointment;
import Model.Employee;
import Model.Patient;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeInLeft;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

import static java.awt.Color.*;

public class Main extends Application {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    private static Stage primaryStage           = new Stage();
    private static BorderPane Outer             = new BorderPane();
    private static BorderPane RIS_Container     = new BorderPane();
    private static ArrayList<URL> backNodeList = new ArrayList<>();
    private static Scene MainScene              = new Scene(Outer,800, 450);
    private static Patient patientFocus         = new Patient();
    private static Appointment appointmentFocus = new Appointment();
    private static Employee sessionUser         = new Employee();
    public static  Stage popup                  = new Stage();
    public static  BoxBlur bb                   = new BoxBlur();


    ////////////////
    //View Setters//
    ////////////////

    //Called to set the main view
    //sets the right to null in case of expanded view
    //Pushes a node onto our back button list
    public static void setCenterPane(String fxmlName)throws Exception{
        try {
            backNodeList.add(Main.class.getResource("../View/"+fxmlName));
            RIS_Container.setRight(null);
            RIS_Container.setCenter(FXMLLoader.load(Main.class.getResource("../View/"+fxmlName)));
            new FadeInLeft(RIS_Container).play();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void setCenterPane(String fxmlName, boolean fade)throws Exception{
        try {
            backNodeList.add(Main.class.getResource("../View/"+fxmlName));
            RIS_Container.setRight(null);
            RIS_Container.setCenter(FXMLLoader.load(Main.class.getResource("../View/"+fxmlName)));
            if(fade){
                new FadeIn(RIS_Container).play();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    //Sets the main view to the top of our back button list
    //Pops the top of the back button list
    public static void setBackPage() throws Exception{
        RIS_Container.setRight(null);
        popBackNodeList();
        RIS_Container.setCenter(FXMLLoader.load(backNodeList.get((backNodeList.size()-1))));
    }

    //Create Popup Window using submitted
    public static void setPopupWindow(String fxmlName) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getResource("../View/" + fxmlName));
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.setAlwaysOnTop(true);
        popup.setTitle("RIS Clinic System");
        popup.getIcons().add(new Image("View/icons/icon.png"));
        popup.show();
        Outer.setEffect(bb);
        new FadeIn(root).play();
    }

    //Determines what all needs to be done when the popup window closes via 'X' button
    private void closeWindowEvent()
    {
        Outer.setEffect(null);
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

    //public static Procedure getProcedureFocus(){return procedureFocus;}
    //public static void setProcedureFocus(Procedure procedureFocus){Main.procedureFocus = procedureFocus;}

    public static Appointment getAppointmentFocus(){ return appointmentFocus; }
    public static void setAppointmentFocus(Appointment appointmentFocus){
        Main.appointmentFocus = appointmentFocus;
    }

    public static Employee getSessionUser() {
        return sessionUser;
    }
    public static void setSessionUser(Employee sessionUser) {
        Main.sessionUser = sessionUser;
    }

    public static void popBackNodeList() {
        try {
            backNodeList.remove(backNodeList.get((backNodeList.size() - 1)));
        } catch (Exception e){}
    }

    //TODO: (Deprecated) Delete at end
    public static ArrayList<URL> getBackNodeList(){
        return backNodeList;
    }


    ////////////////
    //Start Method//
    ////////////////
    @Override
    public void start(Stage stage) throws Exception{
        databaseConnector.getStartConnection();
        Parent root = FXMLLoader.load(getClass().getResource("../View/LoginView.fxml"));

        primaryStage.setTitle("RIS Clinic System");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("View/icons/icon.png"));
        primaryStage.setScene(new Scene(root, 300, 325));

        new FadeIn(root).play();
        //Setup a listener for when the popup window is closed
        popup.setOnCloseRequest(event -> {
            this.closeWindowEvent();
        });

        //Finally show initial stage
        primaryStage.show();
    }

    public static void successfulLogin() throws Exception{
        primaryStage.setResizable(true);
        primaryStage.setScene(MainScene);
        //Set the TOP of the borderPane to our menu
        Outer.setTop(FXMLLoader.load(Main.class.getResource("../View/MenuViews/RIS_Menu.fxml")));
        Outer.setLeft(FXMLLoader.load(Main.class.getResource("../View/MenuViews/RIS_Tabs.fxml")));
        Outer.setCenter(RIS_Container);
        //primaryStage.setFullScreen(true); // set stage to fullscreen
        //primaryStage.setMaximized(true);  //set stage to max size of screen
        //Set the initial start to our PatientList
        //setCenterPane("PatientViews/PatientList.fxml");
        primaryStage.setMaximized(true);
    }

    //Useless but necessary
    public static void main(String[] args) {
        launch(args);
    }
}
