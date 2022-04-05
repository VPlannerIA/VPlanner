package IA.Controllers.projects;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class projectButtonController implements Initializable {
    @FXML
    private Text dateText;

    @FXML
    private Text buttonSubject;

    @FXML
    private Text buttonTitle;
    @Override
    public void initialize(URL location, ResourceBundle resources){
        buttonSubject.setText(ProjectController.buttonSubject);
        buttonTitle.setText(ProjectController.buttonTitle);
        dateText.setText(ProjectController.buttonDate);
    }
}
