package IA.Controllers;

import IA.Main;
import IA.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class signUpController {
    private SceneChanger changer = new SceneChanger();


    @FXML
    private Button logInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Text signUpText;

    @FXML
    private Text incorrectText;

    @FXML
    private TextField usernameField;


    @FXML
    private TextField nameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField verifyPassField;

    @FXML
    private Text warningText;

    private TextField[] fields = new TextField[]{usernameField,nameField,lastNameField,emailField,phoneNumberField};
    private PasswordField[] pFields = new PasswordField[]{passwordField, verifyPassField};

    @FXML
    void onSignUp(ActionEvent event) throws SQLException, IOException {
        String username = usernameField.getText();
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();
        if(username.length() == 0){
            warningMessage(usernameField, "Please input a username");
        }
        else if(name.length() == 0){
            warningMessage(nameField, "Please input a name");
        }
        else if(lastName.length() == 0){
            warningMessage(lastNameField, "Please input a last name");
        }
        else if(email.length() == 0){
            warningMessage(emailField, "Please input an e-mail");
        }
        else if(password.length() == 0){
            warningMessage(passwordField, "Please input a password");
        }
        else if(!isEmail(email)){
            warningMessage(emailField, "Please input a valid e-mail address");
        }
        else if(!password.equals(verifyPassField.getText())){
            warningMessage(verifyPassField, "Passwords do not match");
        }
        else if(Main.db.usernameExist(username)){
            warningMessage(usernameField, "Username already in use");
        }
        else if(Main.db.emailExist(email)){
            warningMessage(emailField, "E-mail already in use");
        }
        else if(Main.db.phoneExist(phoneNumber) && phoneNumber.length()!=0){
            warningMessage(phoneNumberField, "Phone number already in use");
        }
        else {
            Main.db.createUser(username,password,email,name,lastName,phoneNumber);
            changer.loginScene();
        }

    }

    @FXML
    void onLogIn(ActionEvent event) throws IOException {
        changer.loginScene();
    }


    private void warningMessage(TextField field, String message){
        resetStyle();
        warningText.setText(message);
        warningText.setFill(Color.valueOf("#F04747"));
        warningText.setLayoutX(150- warningText.getLayoutBounds().getWidth()/2);
        field.setStyle("-fx-border-color: #F04747; -fx-background-color: #3C3F41; -fx-text-fill: #FFFFFF; -fx-border-radius: 4;");
    }
    private void warningMessage(PasswordField field, String message){
        resetStyle();
        warningText.setText(message);
        warningText.setLayoutX(150- warningText.getLayoutBounds().getWidth()/2);
        warningText.setFill(Color.valueOf("#F04747"));
        field.setStyle("-fx-border-color: #F04747; -fx-background-color: #3C3F41; -fx-text-fill: #FFFFFF; -fx-border-radius: 4;");

    }

    private void resetStyle(){
        fields = new TextField[]{usernameField,nameField,lastNameField,emailField,phoneNumberField};
        pFields = new PasswordField[]{passwordField, verifyPassField};
        for(TextField field: fields){
            field.setStyle("-fx-background-color: #3C3F41; -fx-text-fill: #FFFFFF;");
        }for(PasswordField field: pFields){
            field.setStyle("-fx-background-color: #3C3F41; -fx-text-fill: #FFFFFF;");
        }
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
