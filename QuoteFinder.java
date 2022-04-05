package IA;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuoteFinder {

    public QuoteFinder() {

    }

    private static boolean loop = true;
    private static String responseString;

    //if quote is less than 100 characters, it gets formatted and then the loop stops
    public static String parseQuote(String responseBody) {
        JSONObject response = new JSONObject(responseBody);
        if (response.getInt("length") < 100) {
            responseString = "\"" + response.getString("content") + "\"\n- " + response.getString("author");
            loop = false;
        }
        return null;
    }

    public String getQuote() throws IOException {
        //loop while the quote from the API is above 99 characters
        while (loop) {
            //create client and request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.quotable.io/random?tags=inspirational")).build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(IA.QuoteFinder::parseQuote).join();
        }
        return responseString;//response string assigned in parseQuote() method

    }
}
