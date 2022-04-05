package IA;

import IA.MYSQL.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static String errorMessage;
    public static Stage errorStage;
    public static Stage mainStage = new Stage();
    public static Database db;
    public static int userId;
    public static String username;
    public static String name;
    public static String lastName;
    public static String phone;
    public static String email;
    public static String instagram;
    public static String facebook;
    public static String twitter;
    public static String fullName;
    public static String encPass;
    public static String databaseIP="jdbc:mariadb://141.144.250.164:3306/vplanner";

    public static int currentSubject;

    public static double stageSizeX;
    public static HBox titleBar;
    public static int sidebarButtonClickedID = 1;
    
    public static int selectedSubjectId = 1;


    public void start(Stage stage) throws Exception {
        mainStage.setTitle("VPlanner: Choose Database");
        mainStage.getIcons().add(new Image(("/IA/assets/Logo/favicon.png")));
        mainStage.setResizable(false); //GUI was not made to be resizable
        Pane main = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/IA/FXML/parts/selectDatabasePage.fxml"))
        );//Use  getClass().getResource() on FXML loaders
        Scene scene = new Scene(main);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static Stage getStage() {
        return mainStage;
    }
    public static String[] getColors() {
        String backgroundColor = "#2B2B2B";
        String textColor = "#FFFFFF";
        String accentColor = "#8C54A1";
        String secondBackgroundColor = "#3C3F41";
        String green = "#00ff7f";
        String red = " #F04747";
        String yellow = "#ebc660";
        String accentColorDark = "#5f386e";
        String accentColorDarker = "#4f285e";
        return new String[]{backgroundColor, secondBackgroundColor, textColor, accentColor, accentColorDark, accentColorDarker, green, red, yellow};
    }

    public static String getWelcomeMessage() {
        String greeting;
        int hours = LocalDateTime.now().getHour();
        if (hours >= 3 && hours <= 12) {
            return "Good Morning, " + name + "!";
        } else if (hours >= 12 && hours <= 16) {
            return "Good Afternoon, " + name + "!";
        } else if (hours >= 16 && hours <= 21) {
            return "Good Evening, " + name + "!";
        }
        return "Good Night, " + name + "!";
    }
}
