package IA.Controllers.todo;

import IA.ErrorHandler;
import IA.Main;
import IA.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ResourceBundle;

public class toDoController implements Initializable {
    private final SceneChanger setScene = new SceneChanger();
    private int currentView;
    private double contentPaneX, contentPaneY;

    @FXML
    private Pane darkenerPane;


    @FXML
    private ListView<Button> pendingListView;

    @FXML
    private ListView<Button> doneListView;

    @FXML
    private Pane viewToDoPane;

    @FXML
    private Button closeButton;

    @FXML
    private Button doneButton;

    @FXML
    private Text viewToDoText;

    @FXML
    private Text viewToDoDetails;

    @FXML
    void toDoCreate() {
        darkenerPane.setVisible(true);
        createToDoPane.setVisible(true);
    }

    private final ObservableList<Button> pendingToDo = FXCollections.observableArrayList();
    private final ObservableList<Button> doneToDo = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeView();
        initializeCreatePanel();
        viewToDoPane.setVisible(false);
        try {
            ResultSet rs = Main.db.getToDO();
            while (rs.next()) {
                if (rs.getBoolean("done")) {
                    doneToDo.add(ButtonMaker(rs.getString("title"), rs.getString("details"), rs.getString("color"), rs.getInt("id"), true));
                } else {
                    pendingToDo.add(ButtonMaker(rs.getString("title"), rs.getString("details"), rs.getString("color"), rs.getInt("id"), false));
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            ErrorHandler.display(throwable.getMessage());
        }
        Collections.reverse(doneToDo);
        Collections.reverse(pendingToDo);
        pendingListView.setItems(pendingToDo);
        doneListView.setItems(doneToDo);

        darkenerPane.setOnMouseClicked(e -> close());

    }

    private static String pendingButtonStyle;
    private static String doneButtonStyle;

    private String contentPaneStyle;


    private void initializeView() {
        pendingButtonStyle = "-fx-background-color: %s; " +
                "-fx-text-fill: %s; " +
                "-fx-font-size: %s; " +
                "-fx-pref-width: 328; " +
                "-fx-wrap-text: true; " +
                "-fx-alignment: CENTER; " +
                "-fx-border-color: %s;" +
                "-fx-border-width: 0 0 0 5;" +
                "-fx-background-insets: 0;";

        doneButtonStyle = "-fx-background-color: %s; " +
                "-fx-text-fill: %s; " +
                "-fx-font-size: %s; " +
                "-fx-pref-width: 328; " +
                "-fx-wrap-text: true; " +
                "-fx-alignment: CENTER; " +
                "-fx-border-color: %s;" +
                "-fx-border-width: 0 5 0 0;" +
                "-fx-background-insets: 0;";

        contentPaneStyle = "-fx-background-radius: 10;-fx-background-color: #3C3F41; -fx-border-radius:  10; -fx-border-color: %s; -fx-border-width: 5;";
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #F04747; -fx-font-size: 25;-fx-cursor: hand;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color:  #F04747; -fx-font-size: 24;"));
        viewToDoPane.setOnDragDetected(e -> {
            contentPaneX = e.getSceneX();
            contentPaneY = e.getSceneY();
        });
        viewToDoPane.setOnMouseDragged(e -> {
            viewToDoPane.setLayoutX(e.getSceneX() - contentPaneX);
            viewToDoPane.setLayoutY(e.getSceneY() - contentPaneY);
        });

    }

    public Button ButtonMaker(String title, String details, String color, int id, boolean isDone) {
        Button button = new Button(title);
        button.wrapTextProperty().setValue(true);
        button.textAlignmentProperty().set(TextAlignment.CENTER);
        if (isDone) {
            button.getStylesheets().add(String.valueOf(getClass().getResource("/IA/FXML/parts/css/toDoDoneButton.css")));
            button.setAlignment(Pos.TOP_LEFT);
            setButtonProperties(color, button, doneButtonStyle, "rgba(255,255,255,0.4)");
        } else {
            setButtonProperties(color, button, pendingButtonStyle, "white");
        }
        button.setOnAction(e -> viewToDo(title, details, color, id, isDone));
        return button;
    }

    private static void setButtonProperties(String color, Button button, String buttonStyle, String textColor) {
        setButtonStyle(color, button, buttonStyle, textColor, "white");
    }

    public static void setButtonStyle(String color, Button button, String buttonStyle, String textColor, String hoverColor) {
        button.setStyle(String.format(buttonStyle, String.format(color, "0.5"), textColor, 25, String.format(color, "1")));
        button.setOnMouseEntered(e -> button.setStyle(String.format(buttonStyle, String.format(color, "0.5"), hoverColor, 26, String.format(color, "1")) + "-fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle(String.format(buttonStyle, String.format(color, "0.5"), textColor, 25, String.format(color, "1"))));
    }

    public void viewToDo(String title, String details, String color, int id, boolean done) {
        viewToDoText.setText(title);
        currentView = id;
        if (done) {
            doneButton.setVisible(false);
            closeButton.setLayoutX(225);
        } else {
            doneButton.setVisible(true);
            closeButton.setLayoutX(150);
        }
        darkenerPane.setVisible(true);
        viewToDoPane.setStyle(String.format(contentPaneStyle, String.format(color, "1")));
        viewToDoDetails.setText(details);
        viewToDoPane.setVisible(true);
    }

    @FXML
    void onHandleViewDone() throws SQLException {
        Main.db.finishToDo(currentView);
        close();
    }

    @FXML
    void oneHandleViewClose() {
        close();
    }

    void close() {
        try {
            setScene.toDoScene();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            ErrorHandler.display(ioException.getMessage());
        }
    }

    @FXML
    private Pane createToDoPane;

    @FXML
    private TextField createTitleField;

    @FXML
    private TextArea createDetailField;

    @FXML
    private ColorPicker createColorPicker;

    @FXML
    private Button createButton;

    @FXML
    void onCreateTodo() throws SQLException {
        String title = createTitleField.getText();
        String details = createDetailField.getText();
        String color = toRgbString(createColorPicker.getValue());
        Main.db.createToDo(title, details, color);
        close();
    }

    private void initializeCreatePanel() {
        createButton.setOnMouseEntered(e -> createButton.setFont(Font.font(27)));
        createButton.setOnMouseExited(e -> createButton.setFont(Font.font(26)));
        createColorPicker.valueProperty().addListener(e -> {
            createButton.setStyle("-fx-background-color:" + String.format(toRgbString(createColorPicker.getValue()), "1") + " ;");
            createToDoPane.setStyle("-fx-border-color:" + String.format(toRgbString(createColorPicker.getValue()), "1") + ";-fx-background-radius: 10;-fx-border-radius: 10 ; -fx-background-color: #3C3F41;-fx-border-width: 5;");
        });

        createToDoPane.setOnMouseDragged(e -> {
            createToDoPane.setLayoutX(e.getSceneX());
            createToDoPane.setLayoutY(e.getSceneY());
        });
    }

    private String toRgbString(Color c) {
        return "rgba(" + to255Int(c.getRed()) + "," + to255Int(c.getGreen()) + "," + to255Int(c.getBlue()) + ", %s)";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }

}