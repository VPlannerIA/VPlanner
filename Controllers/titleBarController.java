package IA.Controllers;

import IA.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import jfxtras.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class titleBarController implements Initializable {

    double x,y;

    @FXML
    private HBox titleBar;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    void onClose(ActionEvent event) {
        Main.mainStage.close();
    }

    @FXML
    void onMinimize(ActionEvent event) {
        Main.mainStage.setIconified(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        titleBar.setOnMousePressed(e -> {
            x= e.getSceneX();
            y=e.getSceneY();
        });
        titleBar.setOnMouseDragged(e -> {
            Main.mainStage.setX(e.getScreenX() - x);
            Main.mainStage.setY(e.getScreenY()-y);
        });
    }

}
