package IA.Controllers.social;

import IA.ErrorHandler;
import IA.Main;
import IA.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class socialController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();

    public static String contactName;
    public static String contactSurname;
    public static String contactPhone;
    public static String contactMail;
    public static int currentContactId;

    @FXML
    private ListView<VBox> contactsListView;

    @FXML
    private Button addButton;

    @FXML
    private Pane darkenerPane;

    @FXML
    private Pane createContactPane;

    @FXML
    private TextField contactNameTextField;

    @FXML
    private TextField contactSurnameTextField;

    @FXML
    private TextField contactPhoneTextField;

    @FXML
    private TextField contactMailTextField;

    @FXML
    private Pane viewContactPane;

    @FXML
    private Text contactNameText;

    @FXML
    private Text contactMailText;

    @FXML
    private Text contactPhoneText;

    @FXML
    private Text contactSurText;

    public socialController() throws MalformedURLException {
    }

    @FXML
    void onAddButton(ActionEvent event) {
        darkenerPane.setVisible(true);
        createContactPane.setVisible(true);
    }
    @FXML
    void onCreateContact(ActionEvent event) throws SQLException, IOException {
        Main.db.createContact(contactNameTextField.getText(), contactSurnameTextField.getText(),contactPhoneTextField.getText(),contactMailTextField.getText());
        close(true);
    }
    @FXML
    void onDeleteContact(ActionEvent event) throws IOException, SQLException {
        Main.db.deleteContact(currentContactId);
        close(true);
    }

    private final ObservableList<VBox> contactButtonList = FXCollections.observableArrayList();
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
            VBox marginVbox = new VBox();
            marginVbox.setStyle("-fx-pref-height: 80; -fx-background-color: rgba(0,0,0,0);");
            contactButtonList.add(marginVbox);
            ResultSet rs=  Main.db.getContacts();
            char currentCharacter = '#';
            if(rs.next()){
                currentCharacter = rs.getString("surname").charAt(0);
                contactButtonList.add(letterSeperator(currentCharacter));
                contactButtonList.add(createContainer(rs.getString("name"),rs.getString("surname"), rs.getString("phone"), rs.getString("mail"), rs.getInt("id")));

            }
            while(rs.next()){
                if(currentCharacter != rs.getString("surname").charAt(0)){
                    currentCharacter = rs.getString("surname").charAt(0);
                    contactButtonList.add(letterSeperator(currentCharacter));
                }
                contactButtonList.add(createContainer(rs.getString("name"),rs.getString("surname"), rs.getString("phone"), rs.getString("mail"), rs.getInt("id")));
            }
                contactsListView.setItems(contactButtonList);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }
    }

    private VBox letterSeperator(char currentCharacter) {
        Label txt = new Label("    " + Character.toUpperCase((currentCharacter)));
        txt.setStyle("-fx-text-fill: white;-fx-font-size: 24");
        return new VBox(txt);
    }
    private VBox createContainer(String name, String surname, String phone, String mail, int id) throws IOException {
        contactName = name;
        contactSurname = surname;
        contactPhone = phone;
        contactMail = mail;
        URL buttonURL = getClass().getResource("/IA/FXML/parts/social/contactsButton.fxml");
        VBox vbox = FXMLLoader.load(buttonURL);
        vbox.getChildren().get(0).setOnMouseClicked(e ->{
            openContact(id,name,surname,phone,mail);
        });
        return vbox;
    }

    private void openContact(int id, String name, String surname, String phone, String mail) {
        contactNameText.setText("Name: " + name);
        contactSurText.setText("Surname: " + surname);
        contactPhoneText.setText("Phone: " + phone);
        contactMailText.setText("E-Mail: " + mail);
        currentContactId = id;
        darkenerPane.setVisible(true);
        viewContactPane.setVisible(true);

    }

    private void close(Boolean reload) throws IOException {
        if(reload){sceneChanger.socialScene();}

        else{
            darkenerPane.setVisible(false);
            createContactPane.setVisible(false);
            viewContactPane.setVisible(false);
        }
    }

}
