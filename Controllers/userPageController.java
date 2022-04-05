package IA.Controllers;

import IA.MYSQL.Database;
import IA.Main;
import IA.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class userPageController implements Initializable {

    private SceneChanger changer = new SceneChanger();

    @FXML
    private TextField usernameField;

    @FXML
    private TextField twitterField;

    @FXML
    private TextField facebookField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField instaField;

    @FXML
    private Button logOffButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField fullNameField;

    @FXML
    private Button changeButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    void onLogOff(ActionEvent event) throws IOException {
        Main.username = null;
        Main.name = null;
        Main.lastName = null;
        Main.phone = null;
        Main.email = null;
        Main.instagram = null;
        Main.facebook = null;
        Main.twitter = null;
        changer.loginScene();
    }

    @FXML
    void onDelete(ActionEvent event) throws IOException, SQLException{
        Main.db.deleteUser();
        changer.loginScene();
    }
    @FXML
    void onChange(ActionEvent event) throws IOException, SQLException {
        if (check()){
            if (Database.getSHA512(passwordField.getText()).equals(Main.encPass)) {
                String name = fullNameField.getText().split(" ")[0];
                String lastName = fullNameField.getText().split(" ")[1];
                Main.db.updateAccount(usernameField.getText(), mailField.getText(), name, lastName, phoneField.getText());
                changer.userScene();
            } else {
                errorMessage.setVisible(true);
                passwordField.setStyle("-fx-background-color: #3C3F41; -fx-border-color: #F04747; -fx-border-radius: 8; -fx-text-fill: #FFFFFF;");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        changeButton.setVisible(false);
        passwordField.setVisible(false);
        errorMessage.setVisible(false);
        usernameField.setText(Main.username);
        twitterField.setText(Main.twitter);
        facebookField.setText(Main.facebook);
        mailField.setText(Main.email);
        phoneField.setText(Main.phone);
        instaField.setText(Main.instagram);
        fullNameField.setText(Main.fullName);
        TextField[] fields = new TextField[]{fullNameField,usernameField,mailField,phoneField,twitterField,facebookField,instaField};
        for(TextField field: fields){
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if(changeTest()){
                    changeButton.setVisible(true);
                    passwordField.setVisible(true);
                }else{
                    changeButton.setVisible(false);
                    passwordField.setVisible(false);
                    resetStyle();
                    passwordField.setStyle("-fx-background-color: #3C3F41; -fx-border-color: #2B2B2B; -fx-border-radius: 8; -fx-text-fill: #FFFFFF;");
                }
            });
        }
    }
    public boolean changeTest(){
        if(!usernameField.getText().equals(Main.username)){return true;}
        if(!twitterField.getText().equals(Main.twitter)){return true;}
        if(!facebookField.getText().equals(Main.facebook)){return true;}
        if(!mailField.getText().equals(Main.email)){return true;}
        if(!phoneField.getText().equals(Main.phone)){return true;}
        if(!instaField.getText().equals(Main.instagram)){return true;}
        if(!fullNameField.getText().equals(Main.fullName)){return true;}
        return false;
    }

    public boolean check() throws SQLException {
        String username = usernameField.getText();
        String name = fullNameField.getText().split(" ")[0];
        String lastName = fullNameField.getText().split(" ")[1];
        String email = mailField.getText();
        String phoneNumber = phoneField.getText();
        if(phoneNumber.length() == 0){
            phoneNumber = null;
        }
        if(username.length() == 0){
            warningMessage(usernameField, "Please input a username");
            return false;
        }
        else if(name.length() == 0){
            warningMessage(fullNameField, "Please input a name");
            return false;
        }
        else if(lastName.length() == 0){
            warningMessage(fullNameField, "Please input a last name");
            return false;
        }
        else if(email.length() == 0){
            warningMessage(mailField, "Please input an e-mail");
            return false;
        }
        else if(!isEmail(email)){
            warningMessage(mailField, "Please input a valid e-mail address");
            return false;
        }
        else if(passwordField.getText().length() == 0){
            warningMessage(passwordField, "Please input a password");
            return false;
        }
        else if(Main.db.usernameExist(username)){
            warningMessage(usernameField, "Username already exists");
            return false;
        }
        else if(Main.db.emailExist(email)){
            warningMessage(mailField, "E-mail already in use");
            return false;
        }
        else if(Main.db.phoneExist(phoneNumber)){
            warningMessage(phoneField, "Phone number already in use");
            return false;
        }
        return true;
    }
    private void warningMessage(TextField field, String message){
        resetStyle();
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        field.setStyle("-fx-border-color: #F04747; -fx-background-color: #3C3F41; -fx-border-radius: 8; -fx-text-fill: #FFFFFF;");
    }
    private void warningMessage(PasswordField field, String message){
        resetStyle();
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        field.setStyle("-fx-border-color: #F04747; -fx-background-color: #3C3F41; -fx-border-radius: 8; -fx-text-fill: #FFFFFF;");

    }
    private void resetStyle(){
        errorMessage.setVisible(false);
        TextField[] fields = new TextField[]{usernameField,fullNameField,mailField,phoneField};
        for(TextField field: fields) {
            field.setStyle("-fx-background-color: #3C3F41; -fx-border-color: #2B2B2B; -fx-border-radius: 8; -fx-text-fill: #FFFFFF;");
        }
        passwordField.setStyle("-fx-background-color: #3C3F41; -fx-border-color: #2B2B2B; -fx-border-radius: 8; -fx-text-fill: #FFFFFF;");
    }

    private boolean isEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

}
