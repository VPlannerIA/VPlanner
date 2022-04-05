package IA.Controllers;

import IA.ErrorHandler;
import IA.MYSQL.Database;
import IA.Main;
import IA.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class selectDatabaseController {
    private final SceneChanger sceneChanger = new SceneChanger();

    @FXML
    private Pane mainPane;

    @FXML
    void onLocalHost(ActionEvent event) throws IOException {
        Main.databaseIP = "jdbc:mariadb://localhost/vplanner";
        Main.db = new Database();
        sceneChanger.loginScene();
    }

    @FXML
    void onServer(ActionEvent event) throws IOException {
        Main.db = new Database();
        sceneChanger.loginScene();
    }

}
