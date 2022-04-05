package IA.Controllers.subject;

import IA.ErrorHandler;
import IA.Main;
import IA.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SubjectSelectionController implements Initializable {

    @FXML
    private ListView<HBox> subjectListView;

    @FXML
    private Button addButton;

    @FXML
    private Pane darkenerPane;

    @FXML
    private Pane createSubjectPane;

    @FXML
    private Button createButton;

    @FXML
    private TextField subjectNameTextField;

    @FXML
    private TextField classTextField;

    @FXML
    private TextField teacherTextField;



    @FXML
    void onAddButton(ActionEvent event) {
        darkenerPane.setVisible(true);
        createSubjectPane.setVisible(true);
    }


    @FXML
    void onCreateSubject(ActionEvent event) throws SQLException, IOException {
        Main.db.createSubject(subjectNameTextField.getText(),teacherTextField.getText(), classTextField.getText());
        closeCreateButtonPane(true);
    }

    private final ObservableList<Button> subjectButtonList = FXCollections.observableArrayList();
    private final ObservableList<HBox> tabledButtons = FXCollections.observableArrayList();
    private final SceneChanger sceneChanger = new SceneChanger();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ResultSet rs = Main.db.getUserSubjects();
            while(rs.next()){
                Button button = new Button(rs.getString("name"));
                button.setCursor(Cursor.HAND);
                button.setStyle(" -fx-border-radius: 10; -fx-border-width: 2 ;" +
                        "-fx-background-color: rgba(140,84,161,1); -fx-text-fill: #fff; -fx-pref-width: 200;" +
                        " -fx-background-radius: 10;-fx-font-size: 15pt");
                int id = rs.getInt("id");
                button.setOnAction(e -> {
                    try {
                        openSubjectPage(id);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        ErrorHandler.display(ex.getMessage());
                    }
                });
                subjectButtonList.add(button);
            }

            HBox paddingHbox = new HBox();
            paddingHbox.setPrefHeight(80);
            tabledButtons.add(paddingHbox);
            displaySubjects();
            subjectListView.setItems(tabledButtons);

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }
        darkenerPane.setOnMouseClicked(e -> {
            try {
                closeCreateButtonPane(false);
            } catch (IOException ex) {
                ex.printStackTrace();
                ErrorHandler.display(ex.getMessage());
            }
        });

    }

    private void closeCreateButtonPane(Boolean refresh) throws IOException {
        if(!refresh){
            darkenerPane.setVisible(false);
            createSubjectPane.setVisible(false);
        }else{
            sceneChanger.subjectScene();
        }
    }

    private void openSubjectPage(int id) throws IOException {
        Main.selectedSubjectId = id;
        sceneChanger.subjectPageScene();
    }
    private void displaySubjects() {
        int num = 0;
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(5, 0, 5, 0));
        for(Button i: subjectButtonList){
            hbox.getChildren().addAll(i);
            num++;
            if(num%3==0) {
                tabledButtons.add(hbox);
                hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(5);
                hbox.setPadding(new Insets(5, 0, 5, 0));
            }
        }
        tabledButtons.add(hbox);
    }
}
