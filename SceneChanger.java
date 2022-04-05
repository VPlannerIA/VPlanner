package IA;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SceneChanger {
    public SceneChanger(){}


    public void mainScene() throws IOException {
        IA.Main.getStage().setTitle("VPlanner");
        Main.sidebarButtonClickedID = 1;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/mainMenuPage.fxml"));

    }

    public void loginScene() throws IOException {
        IA.Main.getStage().setTitle("VPlanner: Login");
        constructSecondaryPanel(getClass().getResource("/IA/FXML/parts/loginPage.fxml"));

    }

    private void constructSecondaryPanel(URL mainURL) throws IOException {
        Pane main = FXMLLoader.load(mainURL);
        Main.getStage().setScene(new Scene(main));
    }

    public void signUpScene() throws IOException {
        IA.Main.getStage().setTitle("VPlanner: SingUp");
        constructSecondaryPanel(getClass().getResource("/IA/FXML/parts/signUpPage.fxml"));
    }
    public void userScene() throws IOException{
        IA.Main.getStage().setTitle("VPlanner: User");
        Main.sidebarButtonClickedID=0;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/userPane.fxml"));
    }
    public void calendarScene() throws IOException{
        IA.Main.getStage().setTitle("VPlanner: Calendar");
        Main.sidebarButtonClickedID = 4;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/calendar/calendarPage.fxml"));
    }
    public void subjectScene() throws IOException{
        IA.Main.getStage().setTitle("VPlanner: Subjects");
        Main.sidebarButtonClickedID = 2;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/subjects/subjectChoosePage.fxml"));
    }
    public void subjectPageScene() throws IOException{
        IA.Main.getStage().setTitle("VPlanner: Subject");
        Main.sidebarButtonClickedID = 2;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/subjects/subjectPage.fxml"));
    }

    private void constructMainPanel(URL url) throws IOException {
        Pane main = FXMLLoader.load(url);
        HBox contentContainer = new HBox(getSidebarPane(),main);
        Main.getStage().setScene(new Scene(contentContainer));
    }
    private static Pane getSidebarPane() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(SceneChanger.class.getResource("/IA/FXML/parts/sideBar.fxml")));
    }
    public void projectScene() throws IOException {
        IA.Main.getStage().setTitle("VPlanner: Projects");
        Main.sidebarButtonClickedID = 3;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/projects/projectPage.fxml"));
    }
    public void socialScene() throws IOException{
        IA.Main.getStage().setTitle("VPlanner: Contacts");
        Main.sidebarButtonClickedID = 6;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/social/socialPage.fxml"));
    }
    public void toDoScene() throws IOException {
        IA.Main.getStage().setTitle("VPlanner: To-Do");
        Main.sidebarButtonClickedID = 5;
        constructMainPanel(getClass().getResource("/IA/FXML/parts/todo/toDoPage.fxml"));
    }

}
