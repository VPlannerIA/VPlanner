package IA.Controllers.subject;

import IA.ErrorHandler;
import IA.Main;
import IA.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SubjectController implements Initializable{

    private final SceneChanger sceneChanger = new SceneChanger();


    @FXML
    private ListView<Label> projectListView;
    @FXML
    private Text titleText;

    @FXML
    private Label maxGradeField;

    @FXML
    private Button addGradeButton;

    @FXML
    private TextField gradeTextField;

    @FXML
    private TextField ratioTextField;

    @FXML
    private Label avgGradeField;

    @FXML
    private Label minGradeField;

    @FXML
    private TextField teacherTextField;

    @FXML
    private Label absText;

    @FXML
    private Button minusAbsBut;

    @FXML
    private Button plusAbsBut;

    @FXML
    private TextField classTextField;

    @FXML
    private Button changeInfoButton;

    @FXML
    void onAddGrade(ActionEvent event) {
       try {
            double grade = (Double.parseDouble(gradeTextField.getText()) * 100) / Integer.parseInt(ratioTextField.getText());
           Main.db.addGrade(grade);
           sceneChanger.subjectPageScene();
        }catch (NumberFormatException | SQLException | IOException ex){
            ex.printStackTrace();
            ErrorHandler.display(ex.getMessage());
       }

    }

    @FXML
    void onChange(ActionEvent event) throws IOException, SQLException {
        Main.db.changeSubjectInfo(teacherTextField.getText(),classTextField.getText(),Integer.parseInt( absText.getText()));
        sceneChanger.subjectPageScene();
    }

    @FXML
    void onMinusABs(ActionEvent event) {
        absencesCounter(-1);
    }

    @FXML
    void onPlusAbs(ActionEvent event) {
        absencesCounter(1);
    }

    @FXML
    void onSubjectDelete(ActionEvent event) throws SQLException, IOException {
        Main.db.deleteSubject(Main.selectedSubjectId);
        sceneChanger.subjectScene();
    }
    private String teacher;
    private String _class;
    private int absences;

    private final ObservableList<Label> projectList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int subjectId = Main.selectedSubjectId;
        int[] ratios = new int[]{7,20,100};
        try {
            ResultSet rs = Main.db.getSubjectInformation(subjectId);
            rs.next();
            titleText.setText(rs.getString("name"));
            titleText.setLayoutX((701.0-titleText.getBoundsInParent().getWidth())/2);
            teacher=rs.getString("teacher");
            teacherTextField.setText(teacher);
            _class=rs.getString("class");
            classTextField.setText(_class);
            absences = rs.getInt("absences");
            absText.setText(String.valueOf(absences));
            ResultSet projectRs = Main.db.getProjectsBySubjectId();
            while(projectRs.next()){
                projectList.add(createLabel(projectRs.getString("title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }
        projectListView.setItems(projectList);
        teacherTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            changeInfoButton.setVisible(checkChanges());
        });
        classTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            changeInfoButton.setVisible(checkChanges());
        });

        try {
            plotGrades();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }

    }
    private Label createLabel(String title){
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: rgba(140,84,161,0.5);" +
                "-fx-pref-width: 150;" +
                " -fx-background-radius: 20 5 5 20;" +
                " -fx-border-color: #2b2b2b rgb(140,84,161) #2b2b2b #2b2b2b;" +
                " -fx-border-width: 2 5 3 0;" +
                " -fx-font-size: 18;");
        lbl.setAlignment(Pos.CENTER_RIGHT);
        return lbl;
    }
    private void plotGrades() throws SQLException {
        double[] grades = Main.db.getGrades();
        Arrays.sort(grades);
        if(grades.length > 0){
            minGradeField.setText(Math.round(grades[0]) + "%");
            maxGradeField.setText(Math.round(grades[grades.length - 1]) + "%");
            double avg = 0.0;
            for (double i : grades) {
                avg += i;
            }
            avg /= grades.length;
            avgGradeField.setText(Math.round(avg) + "%");
        }
    }

    private boolean checkChanges(){
        if(!teacher.equals(teacherTextField.getText())){return true;}
        else if(!_class.equals(classTextField.getText())){return true;}
        else return !String.valueOf(absences).equals(absText.getText());
    }
    private void absencesCounter(int num){
        int newAbs = Integer.parseInt(absText.getText())+num;
        if (newAbs < 0){newAbs=0;}
        absText.setText(String.valueOf(newAbs));
        changeInfoButton.setVisible(checkChanges());
    }

}
