package IA.Controllers;

import IA.ErrorHandler;
import IA.Main;
import IA.QuoteFinder;
import IA.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class mainMenuController implements Initializable {
    @FXML
    private Button pendingProjectsButton;

    @FXML
    private Button toDoButton;

    @FXML
    private Button eventsTodayButton;


    @FXML
    private Text quoteLabel;

    @FXML
    private Text welcomeLabel;

    private int eventsToday;
    private int toDos;
    private int projects;



    private final SceneChanger changer = new SceneChanger();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            QuoteFinder quoteFinder =new QuoteFinder();
            quoteLabel.setText(quoteFinder.getQuote());
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }
        welcomeLabel.setText(Main.getWelcomeMessage());
        try {
            toDos = Main.db.amountOfToDo();
            Date today = new Date();
            SimpleDateFormat getDate = new SimpleDateFormat("yyyy-M-dd");
            SimpleDateFormat getTime = new SimpleDateFormat("HH:mm:ss");
            eventsToday = Main.db.amountEventsToday(getDate.format(today),getTime.format(today));
            projects = Main.db.amountOfProjects();


        } catch (SQLException throwable) {
            throwable.printStackTrace();
            ErrorHandler.display(throwable.getMessage());
        }
        int pendingProjects = 3;

        int pendingSubjects = 5;


        Button[] buttons = new Button[]{pendingProjectsButton, toDoButton, eventsTodayButton};
        int[] buttonValues = new int[]{projects, toDos, eventsToday};

        for(int i = 0; i < buttons.length; i++){
            Button button = buttons[i];
            if(buttonValues[i] == 0){
                button.setText(buttons[i].getText() + buttonValues[i] + "!");
            }else {
                button.setText(buttons[i].getText() + buttonValues[i]);
            }
            button.setStyle("-fx-background-color:#8C54A1;");

            if(buttonValues[i] > 2){
                button.setStyle("-fx-background-color:#5f386e;");
            }
            else if(buttonValues[i] >5){
                button.setStyle("-fx-background-color:#4f285e;");
            }
        }

    }
    @FXML
    void onTodoClicked() throws IOException {
        changer.toDoScene();
    }
    @FXML
    void onCalendarClicked() throws IOException {
        changer.calendarScene();
    }
    @FXML
    void onPendingProjects() throws IOException {
        changer.projectScene();
    }

}