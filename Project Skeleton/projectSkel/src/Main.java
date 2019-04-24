import Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new StackPane());
        scene.getStylesheets().add("style.css");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sample.fxml"));
        scene.setRoot(loader.load());
        Controller controller = loader.getController();
        //controller.init();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





















