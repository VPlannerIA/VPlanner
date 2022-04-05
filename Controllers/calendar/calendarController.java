package IA.Controllers.calendar;

import IA.ErrorHandler;
import IA.Main;
import IA.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class calendarController implements Initializable {


    @FXML
    private GridPane calendarGrid;

    @FXML
    private Text titleLabel;

    private String[] colorsOfWeek;

    private int currentYear;
    private int currentMonth;
    private int displayYear;
    private int displayMonth;
    private Calendar todayCal;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date today = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy");
        new SimpleDateFormat("dd/M/yyyy");
        darkenerPane.setOnMouseClicked(e -> {
            try {
                sceneChanger.calendarScene();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                ErrorHandler.display(ioException.getMessage());
            }
        });
        titleLabel.setText(formatter.format(today));
        todayCal = Calendar.getInstance();
        todayCal.setTime(today);
        currentMonth = todayCal.get(Calendar.MONTH);
        currentYear = todayCal.get(Calendar.YEAR);
        displayYear = currentYear;
        displayMonth = currentMonth;
        colorsOfWeek = new String[]{"110, 66, 126", "120, 72, 137", "130, 78, 149",
                "140, 84, 161", "150, 94, 170", "158, 106, 177", "166, 119, 183"};
        constructCalendar();

    }

    public void constructCalendar() {
        calendarGrid.getChildren().clear();
        int[] daysOfEachMonth;
        if ((displayYear % 4 == 0 && displayYear % 100 != 0) || displayYear % 400 == 0) {
            daysOfEachMonth = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        } else {
            daysOfEachMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        }
        int daysInTheMonth = daysOfEachMonth[displayMonth];
        int column = 0, row = 0;
        Calendar cal = new GregorianCalendar();
        cal.set(displayYear, displayMonth, 1);
        int xOffset = cal.get(Calendar.DAY_OF_WEEK);
        if (xOffset == 1) {
            xOffset = 8;
        }
        int counter = 0;
        int startDate = daysOfEachMonth[getFormattedMonth(displayMonth, displayYear, -1)[0]];
        for (int i = startDate - xOffset + 3; i <= startDate; i++) {
            Button button = makeIdleButton("" + i, -1);
            calendarGrid.add(button, counter, 0, 1, 1);
            GridPane.setMargin(button, new Insets(5, 5, 5, 5));
            counter++;
        }
        for (int i = 1; i <= daysInTheMonth; i++) {
            cal.set(displayYear, displayMonth, i);
            row = ((cal.get(Calendar.DAY_OF_MONTH) - 3 + xOffset) % 7);
            Button button = makeButton("" + i, row, isToday(cal, todayCal), getCalendarString(cal));
            calendarGrid.add(button, row, column, 1, 1);
            GridPane.setMargin(button, new Insets(5, 5, 5, 5));
            if (row >= 6) {
                column++;
            }
        }
        if (row >= 6) {
            row = -1;
        }
        int remaining = 41 - (7 * (column) + row);
        for (int i = 1; i <= remaining; i++) {
            Button button = makeIdleButton("" + i, 1);
            cal.set(getFormattedMonth(displayMonth, displayYear, +1)[1], getFormattedMonth(displayMonth, displayYear, +1)[0], i);
            row++;
            calendarGrid.add(button, row, column, 1, 1);
            GridPane.setMargin(button, new Insets(5, 5, 5, 5));
            if (row >=6) {
                column++;
                row = -1;
            }

        }
    }

    private int[] getFormattedMonth(int inputMonth, int inputYear, int change) {
        int month = inputMonth + change;
        int year = inputYear;
        if (month == -1) {
            month = 11;
            year--;
        } else if (month == 11) {
            month = 0;
            year++;
        }
        return new int[]{month, year};

    }

    private String getCalendarString(Calendar cal) {
        return String.format("%s/%s/%s", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    private Button makeIdleButton(String text, int change) {
        Button button = new Button(text);
        button.setCursor(Cursor.HAND);
        button.getStyleClass().add("idleButton");
        button.setOnMouseClicked(e -> refreshCalendar(change));
        return button;
    }

    public Button makeButton(String text, int dayOfWeek, boolean isTodayDate, String date) {
        Button button = new Button(text);
        button.setCursor(Cursor.HAND);
        button.setOnMouseClicked(e -> openPopup(date, colorsOfWeek[dayOfWeek]));
        button.getStyleClass().add("mainButtons");
        if (isTodayDate) {
            button.setStyle("-fx-background-color: rgba(" + colorsOfWeek[dayOfWeek] + ",0.9); -fx-border-color: rgba(" + colorsOfWeek[dayOfWeek] + ", 1);");
        } else {
            button.setStyle("-fx-background-color: rgba(" + colorsOfWeek[dayOfWeek] + ",0.5); -fx-border-color: rgba(" + colorsOfWeek[dayOfWeek] + ", 1);");
        }
        return button;
    }


    @FXML
    void calendarMinus() {
        refreshCalendar(-1);

    }

    @FXML
    void calendarUp() {
        refreshCalendar(1);

    }

    private void refreshCalendar(int change) {
        displayMonth += change;
        if (displayMonth == 12) {
            displayMonth = 0;
            displayYear++;
        } else if (displayMonth == -1) {
            displayMonth = 11;
            displayYear--;
        }
        changeDateLabel();
        constructCalendar();
    }

    private void changeDateLabel() {
        if (currentMonth == displayMonth && currentYear == displayYear) {
            Date today = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy");
            titleLabel.setText(formatter.format(today));
        } else {
            titleLabel.setText(new DateFormatSymbols().getMonths()[displayMonth] + ", " + displayYear);
        }
    }

    public boolean isToday(Calendar cal1, Calendar cal2) {
        return ((cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) && (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)));
    }

    //popup controller
    private final SceneChanger sceneChanger = new SceneChanger();


    @FXML
    private Pane darkenerPane;

    @FXML
    private Pane contentPane;


    @FXML
    private ListView<Button> eventList;

    @FXML
    private Pane detailPane;

    @FXML
    private Text detailTitle;

    @FXML
    private Text detailDetails;

    @FXML
    private Pane connectorPane;

    @FXML
    private Text dateText;

    @FXML
    private Pane createPane;

    @FXML
    private Label startHourLabel;

    @FXML
    private Label startMinLabel;

    @FXML
    private Label endHourLabel;

    @FXML
    private Label endMinLabel;


    @FXML
    private TextField createTitleField;

    @FXML
    private TextArea createDetailField;


    @FXML
    void onCreateEvent() throws SQLException {
        Main.db.addCalendarEvent(createTitleField.getText(), createDetailField.getText(), getFormattedDate().replace("/", "-"), getStartTime(), getEndTime());
        refresh();
    }

    @FXML
    void onEndHourDown() {
        changeValue(endHourLabel, -1, 24);
    }

    @FXML
    void onEndHourUp() {
        changeValue(endHourLabel, 1, 24);
    }

    @FXML
    void onEndMinDown() {
        changeValue(endMinLabel, -1, 59);
    }

    @FXML
    void onEndMinUp() {
        changeValue(endMinLabel, 1, 59);
    }

    @FXML
    void onStartHourDown() {
        changeValue(startHourLabel, -1, 24);
    }

    @FXML
    void onStartHourUp() {
        changeValue(startHourLabel, 1, 24);
    }

    @FXML
    void onStartMinDown() {
        changeValue(startMinLabel, -1, 59);
    }

    @FXML
    void onStartMinUp() {
        changeValue(startMinLabel, 1, 59);
    }

    @FXML
    void openCreate() {
        if (createPane.isVisible()) {
            createPane.setVisible(false);
        } else {
            createPane.setVisible(true);
            detailPane.setVisible(false);
            connectorPane.setVisible(false);
        }
    }

    private void changeValue(Label label, int increment, int max) {
        int time = Integer.parseInt(label.getText());
        incrementTimeValue(label, increment, max, time);
    }

    static void incrementTimeValue(Label label, int increment, int max, int time) {
        time += increment;
        if (time > max) {
            time = 0;
        }
        if (time < 0) {
            time = max;
        }
        if (String.valueOf(time).length() == 1) {
            label.setText("0" + time);
        } else {
            label.setText("" + time);
        }
    }

    private ObservableList<Button> calendarEvents;

    private String color;
    private String date;
    private int detailEventID;

    private String getFormattedDate() {
        String[] dateArray = date.split("/");
        return dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
    }

    public void openPopup(String date, String Color) {

        darkenerPane.setVisible(true);
        contentPane.setVisible(true);
        calendarEvents = FXCollections.observableArrayList();
        this.color = Color;
        this.date = date;
        createPane.setVisible(false);
        darkenerPane.setOnMouseClicked(e -> {
            try {
                sceneChanger.calendarScene();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                ErrorHandler.display(ioException.getMessage());
            }
        });
        getButtons();
        detailPane.setVisible(false);
        connectorPane.setVisible(false);
        connectorPane.setStyle("-fx-background-color:  #3C3F41; -fx-border-color: rgb(" + color + ");-fx-border-width: 5 0 5 0");
        detailPane.setStyle("-fx-background-color:  #3C3F41; -fx-border-color: rgb(" + color + ");-fx-border-width: 5; -fx-background-radius: 10;-fx-border-radius: 10");
        contentPane.setStyle("-fx-background-color:  #3C3F41; -fx-border-color: rgb(" + color + ");-fx-border-width: 5; -fx-background-radius: 10;-fx-border-radius: 10");
        dateText.setText(date);
    }

    private void getButtons() {
        try {
            ResultSet rs = Main.db.getCalendarEvents(getFormattedDate());
            while (rs.next()) {
                String title = rs.getString("title");
                String details = rs.getString("body");
                String time = rs.getString("time_start") + "\n" + rs.getString("time_end");
                int id = rs.getInt("id");
                calendarEvents.add(makeButton(title, details, time, id));
            }
            eventList.setItems(calendarEvents);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            ErrorHandler.display(throwable.getMessage());
        }
    }

    public void showDetails(String title, String details, int id, Button button) {
        detailEventID = id;
        detailPane.setVisible(true);
        connectorPane.setVisible(true);
        createPane.setVisible(false);
        detailTitle.setText(title);
        detailDetails.setText(details);
        Point2D absButtonCords = button.localToScene(0, 0);
        connectorPane.setLayoutY(absButtonCords.getY() + button.getHeight() / 2 - 35);
        //getHeight/2 to center relative to Button, -35 to center it relative to the Pane
    }

    public Button makeButton(String title, String details, String time, int id) {
        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 12;-fx-text-fill:white");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24;-fx-text-fill: white");
        HBox hbox = new HBox(timeLabel, titleLabel);
        hbox.setSpacing(10);
        Button button = new Button();
        button.setGraphic(hbox);
        button.setPrefWidth(250);
        button.setWrapText(true);
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-text-fill: white;-fx-background-color: rgba(" + color + ", 0.5);-fx-border-color: rgba("
                + color + ", 1); -fx-border-width: 0 3 0 3;");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-text-fill: white;-fx-background-color: rgba(" + color + ", 0.9);-fx-border-color: rgba("
                    + color + ", 1); -fx-border-width: 0 3 0 3;");
            titleLabel.setStyle("-fx-font-size: 25;-fx-text-fill: white");
            timeLabel.setStyle("-fx-font-size: 13;-fx-text-fill:white");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-text-fill: white;-fx-background-color: rgba(" + color + ", 0.5);-fx-border-color: rgba("
                    + color + ", 1); -fx-border-width: 0 3 0 3;");
            titleLabel.setStyle("-fx-font-size: 24;-fx-text-fill: white");
            timeLabel.setStyle("-fx-font-size: 12;-fx-text-fill:white");
        });
        button.setOnMouseClicked(e -> {
            if (detailPane.isVisible() && detailEventID == id) {
                detailPane.setVisible(false);
                connectorPane.setVisible(false);
            } else {
                showDetails(title, details, id, button);
            }
        });
        return button;
    }

    private String getStartTime() {
        return (startHourLabel.getText() + ":" + startMinLabel.getText());
    }

    private String getEndTime() {
        return endHourLabel.getText() + ":" + endMinLabel.getText();
    }

    private void refresh() {
        openPopup(date, color);
    }

}