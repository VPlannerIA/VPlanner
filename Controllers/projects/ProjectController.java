package IA.Controllers.projects;

import IA.ErrorHandler;
import IA.Main;
import IA.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    public static String buttonTitle;
    public static String buttonSubject;
    public static String buttonDate;
    public static int currentProjectId;
    private final SimpleDateFormat df = new SimpleDateFormat("D/M/YY");

    private final SceneChanger sceneChanger = new SceneChanger();
    private final ObservableList<Integer> subjectIds = FXCollections.observableArrayList();
    private final ObservableList<String> subjectNames = FXCollections.observableArrayList();

    @FXML
    private Text noSubjectText;

    @FXML
    private Pane contentPane;

    @FXML
    private ListView<VBox> projectListView;

    @FXML
    private Pane darkenerPane;

    @FXML
    private Pane createProjectPane;

    @FXML
    private Button createButton;

    @FXML
    private TextField projectTitleTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> subjectComboBox;
    @FXML
    private TextArea projectDetailTextArea;

    @FXML
    private Button addButton;

    @FXML
    private Pane projectPane;

    @FXML
    private Button completeButton;

    @FXML
    private Label projectTitleLabel;

    @FXML
    private Text projectSubjectText;

    @FXML
    private Text projectDateText;

    @FXML
    private Text projectDetailsText;

    public ProjectController() throws MalformedURLException {
    }
    @FXML
    void onDeleteProject(ActionEvent event) throws SQLException, IOException {
        Main.db.deleteProject(currentProjectId);
        close(true);
    }

    @FXML
    void onCreateProject(ActionEvent event) throws SQLException, IOException {
        Main.db.addProject(projectTitleTextField.getText(), projectDetailTextArea.getText(), datePicker.getValue(), getSubjectId());
        close(true);
    }

    private int getSubjectId() {
        return subjectIds.get(subjectNames.indexOf(subjectComboBox.getValue()));
    }

    @FXML
    void onAddButton(ActionEvent event) {
        darkenerPane.setVisible(true);
        createProjectPane.setVisible(true);

    }
    private final ObservableList<VBox> projectList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources){
        darkenerPane.setOnMouseClicked(e -> {
            try {
                close(false);
            } catch (IOException ex) {

                ex.printStackTrace();
                ErrorHandler.display(ex.getMessage());
            }
        });
        try {
            if (!Main.db.hasSubject()) {
                contentPane.setVisible(false);
            } else {
                VBox vbox = new VBox();
                vbox.setStyle("-fx-background-color:  #2b2b2b; -fx-pref-height: 75");
                projectList.add(vbox);
                try {
                    ResultSet subjectRs = Main.db.getUserSubjects();
                    while (subjectRs.next()) {
                        subjectIds.add(subjectRs.getInt("id"));
                        subjectNames.add(subjectRs.getString("name"));
                    }
                    subjectComboBox.setItems(subjectNames);
                    ResultSet rs = Main.db.getProjects();
                    while (rs.next()) {
                        projectList.add(createButton(rs.getInt("id"), rs.getString("title"),
                                Main.db.getSubjectName(rs.getInt("subjectid")),
                                getDate(rs.getString("datedue")),
                                rs.getString("description")));
                    }
                } catch (SQLException | IOException e) {

                    e.printStackTrace();
                    ErrorHandler.display(e.getMessage());
                }
                projectListView.setItems(projectList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }

    }

    private String getDate(String datedue) {
        String[] date = datedue.split("-");
        return date[2] + "/" + date[1] + "/" + date[0];
    }



    private void close(Boolean reload) throws IOException {
        if(reload){sceneChanger.projectScene();}

        else{
            darkenerPane.setVisible(false);
            createProjectPane.setVisible(false);
            projectPane.setVisible(false);
        }

    }
    private VBox createButton(int projectId, String title, String subject, String date, String details) throws IOException {
        buttonTitle = title;
        buttonSubject = subject;
        buttonDate = date;
        URL buttonURL = getClass().getResource("/IA/FXML/parts/projects/projectButton.fxml");
        VBox vbox = FXMLLoader.load(buttonURL);
        vbox.getChildren().get(1).setOnMouseClicked(e ->{
            openProject(projectId,title,subject,date,details);
        });
        return vbox;
    }

    private void openProject(int projectId,String title, String subject, String date, String details) {
        currentProjectId = projectId;
        projectTitleLabel.setText(title);
        projectDateText.setText(date);
        projectDetailsText.setText(details);
        projectSubjectText.setText(subject);
        darkenerPane.setVisible(true);
        projectPane.setVisible(true);

    }

}
