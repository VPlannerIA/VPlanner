package IA;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class ErrorHandler {
    public static void display(String error) {
        Main.errorMessage = error;
        Main.errorStage = new Stage();
        Main.errorStage.setAlwaysOnTop(true); //error message always on top
        Main.errorStage.setTitle("VPlanner: ERROR");
        Main.errorStage.setResizable(false);
        try {
            VBox errorWindow = FXMLLoader.load(Objects.requireNonNull(ErrorHandler.class.getResource("/IA/FXML/alertMessageWindow.FXML")));
            Scene errorScene = new Scene(errorWindow);
            //remove os decoration (title bar etc)
            errorScene.setFill(Color.TRANSPARENT);
            Main.errorStage.initStyle(StageStyle.UNDECORATED);
            Main.errorStage.setScene(errorScene);
            Main.errorStage.getIcons().add(new Image(("/IA/assets/Logo/favicon.png")));
            Main.errorStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
