package Controller;

public class TechController {

    public void setBackPage()throws Exception{
        Main.setBackPage();
    }

    public void setTechApptView() throws Exception{
        Main.setCenterPane("TechViews/TechEntry.fxml");
    }
    public static void setWorkList() throws Exception{
        Main.setCenterPane("TechViews/WorkList.fxml");
    }
}
