package IA.Controllers;

import IA.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import jfxtras.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertWindowController implements Initializable {

    private double x, y;


    @FXML
    private HBox titleBar;

    @FXML
    private Text errorLabel;

    @FXML
    void onClose() {
        Main.errorStage.close();
    }

    @FXML
    void onMinimize() {
        Main.errorStage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleBar.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        titleBar.setOnMouseDragged(e -> {
            Main.errorStage.setX(e.getScreenX() - x);
            Main.errorStage.setY(e.getScreenY() - y);
        });
        errorLabel.setText(Main.errorMessage);
    }


}
