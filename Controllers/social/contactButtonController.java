package IA.Controllers.social;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class contactButtonController implements Initializable {
    @FXML
    private Button contactButton;
    @Override
    public void initialize(URL location, ResourceBundle resources){
        contactButton.setText(socialController.contactSurname + " " + socialController.contactName);
    }

}
