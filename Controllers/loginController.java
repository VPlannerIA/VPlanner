package IA.Controllers;

import IA.ErrorHandler;
import IA.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    private SceneChanger changer = new SceneChanger();

    @FXML
    private Pane mainPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text signUpText;

    @FXML
    private Text incorrectText;


    @FXML
    private Button loginButton;

    @FXML
    void onLogInButton(ActionEvent event) throws IOException {
        login();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        mainPane.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                try {
                    login();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    ErrorHandler.display(ioException.getMessage());
                }
            }
        });
    }

    private void login() throws IOException {
        if(IA.Main.db.getLogin(usernameField.getText(),passwordField.getText())){
            changer.mainScene();
        }
        else{
            incorrectText.setFill(Color.valueOf("#F04747"));
            usernameField.setStyle("-fx-border-color: #F04747; -fx-background-color: #3C3F41; -fx-text-fill: #FFFFFF; -fx-border-radius: 4;");
            passwordField.setStyle("-fx-border-color: #F04747; -fx-background-color: #3C3F41; -fx-text-fill: #FFFFFF; -fx-border-radius: 4;");
        }
    }

    @FXML
    void onSignUpButton(ActionEvent event) throws IOException {
        changer.signUpScene();
    }


}
