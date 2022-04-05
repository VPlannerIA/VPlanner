package IA.Controllers;

import IA.Main;
import IA.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class sidebarController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();


    @FXML
    private Button userButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button subjectButton;

    @FXML
    private Button projectButton;

    @FXML
    private Button calendarButton;

    @FXML
    private Button toDoButton;

    @FXML
    private Button socialButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button[] buttonArray = new Button[]{userButton, mainMenuButton, subjectButton, projectButton, calendarButton, toDoButton, socialButton};
        Button pressedButton = buttonArray[Main.sidebarButtonClickedID];
        pressedButton.getStyleClass().remove("sideButton");
        pressedButton.getStyleClass().add("clickedButton");
    }

    @FXML
    void onCalendarButton() throws IOException {

        sceneChanger.calendarScene();
    }

    @FXML
    void onMainMenuButton() throws IOException {

        sceneChanger.mainScene();
    }

    @FXML
    void onProjectButton() throws IOException {
        sceneChanger.projectScene();
    }



    @FXML
    void onSocialButton() throws IOException {
        sceneChanger.socialScene();
    }

    @FXML
    void onSubjectButton() throws IOException {
        sceneChanger.subjectScene();
    }

    @FXML
    void onToDoButton() throws IOException {
        sceneChanger.toDoScene();
    }

    @FXML
    void onUserButton() throws IOException {
        sceneChanger.userScene();
    }

}
