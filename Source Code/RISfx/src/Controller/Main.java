package Controller;

import Model.Appointment;
import Model.Employee;
import Model.Patient;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeInRightBig;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class Main extends Application {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML
    private MenuItem logout;
    private static Stage primaryStage           = new Stage();
    private static BorderPane Outer             = new BorderPane();
    private static BorderPane RIS_Container     = new BorderPane();
    private static ArrayList<URL> backNodeList  = new ArrayList<>();
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

    /**
     * Loads whatever fxml file it gets passed the name of into the ris container. Adds the name to the back node list
     * so the back buttons know where to go. Has 1 boiyoiyoing on the fade in from left.
     * @param fxmlName
     * @throws Exception
     */
    public static void setCenterPane(String fxmlName)throws Exception{
        try {
            backNodeList.add(Main.class.getResource("../View/"+fxmlName));
            RIS_Container.setRight(null);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/"+fxmlName));
            loader.setClassLoader(Main.class.getClassLoader());
            Parent root = (Parent)loader.load();
            RIS_Container.setCenter(root);
            new FadeInRight(RIS_Container).play();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This sets whatever fxml file gets passed to it to center pane, with the exception that there's a boolean to kill
     * the animation. This way there's no boiyoiyoing when it reloads the page with a new value.
     * @param fxmlName
     * @param fade
     * @throws Exception
     */
    public static void setCenterPane(String fxmlName, boolean fade)throws Exception{
        try {

            setCenterPane(fxmlName);
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

    /**
     * Loads whatever page is in back one in the backnode list when you press back.
     * @throws Exception
     */
    public static void setBackPage() throws Exception{
        RIS_Container.setRight(null);
        popBackNodeList();
        RIS_Container.setCenter(FXMLLoader.load(backNodeList.get((backNodeList.size()-1))));
    }

    //Create Popup Window using submitted

    /**
     * This is the method used for most of the popup windows. Loads the fxml, sets dimensions, forces the popup to always
     * be on top, sets title, sets icon, sets background blur, and has an animation.
     * @param fxmlName
     * @throws Exception
     */
    public static void setPopupWindow(String fxmlName) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/"+fxmlName));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = (Parent)loader.load();
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.setAlwaysOnTop(true);
        popup.setTitle("RIS Clinic");
        popup.getIcons().add(new Image("View/icons/icon.png"));
        popup.show();
        Outer.setEffect(bb);
        new FadeIn(root).play();
    }

    //Determines what all needs to be done when the popup window closes via 'X' button

    /**
     * If someone closes the window, "Outer.setEffect(bb);" in Main.setPopupWindow() would leave RIS client blurred out.
     * this removes the effect on window close event.
     */
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
    public static Stage getPopup(){return popup;}

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

    public static Stage getPrimaryStage(){
        return primaryStage;
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
    /**
     * Gets the database connection. Loads LoginView.fxml.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        databaseConnector.getStartConnection();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("../View/LoginView.fxml"));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = loader.load();

        primaryStage.setTitle("RIS Clinic");
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

    /**
     * Resets dimensions of RIS client, sets to resizable, sets main scene.
     * Opens ris menu on top of outer and ris tabs on left of outer. Sets center to RIS_Container.
     * sets stage to max size of screen.
     * @throws Exception
     */
    public static void successfulLogin() throws Exception{
        primaryStage.setResizable(true);
        primaryStage.setMaxHeight(4000);
        primaryStage.setMaxWidth(4000);
        primaryStage.setScene(MainScene);
        //Set the TOP of the borderPane to our menu
        Outer.setTop(FXMLLoader.load(Main.class.getResource("../View/MenuViews/RIS_Menu.fxml")));
        Outer.setLeft(FXMLLoader.load(Main.class.getResource("../View/MenuViews/RIS_Tabs.fxml")));
        Outer.setCenter(RIS_Container);
        //primaryStage.setFullScreen(true); // set stage to fullscreen
        primaryStage.setMaximized(true);  //set stage to max size of screen
        //Set the initial start to our PatientList
        //setCenterPane("PatientViews/PatientList.fxml");
        primaryStage.setMaximized(true);
    }

    /**
     * On logout, destroys sessionUser, patientFocus, appointmentFocus, backNodeList, and sets RIS_Container to null.
     * Runs Controller.setView() to return to login
     * @throws Exception
     */
    public void logout() throws Exception{
        sessionUser = null;
        patientFocus = null;
        appointmentFocus = null;
        backNodeList = new ArrayList<>();

        RIS_Container.setCenter(null);
        Controller.setView();
    }

    //Useless but necessary
    /**
     * Runs some javafx application method. DO NOT DELETE>>>
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
