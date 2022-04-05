package IA.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class mainController implements Initializable {
    @FXML
    private Button pendingProjectsButton;

    @FXML
    private Button toDoButton;

    @FXML
    private Button eventsTodayButton;

    @FXML
    private Button pendingSubjectButton;

    @FXML
    private Text mainLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //https://api.quotable.io/random?tags=inspirational
        try {
            mainLabel.setText(getQuote());
        } catch (IOException e) {
            e.printStackTrace();
            mainLabel.setText("\"Believe you can, and you’re halfway there\" \n- Theodore Roosevelt");
        }

    }
    public String getQuote() throws IOException {
        String quote = "";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.quotable.io/random?tags=inspirational")).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).thenAccept(System.out::println).join();

        return quote;
    }
}
