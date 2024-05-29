package simplexity.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;
import simplexity.config.SimplexityFileHandler;
import simplexity.messages.Errors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String[] params = query.split("&");
            String twitchCode = "";
            for (String param : params) {
                if (param.startsWith("code=")) {
                    twitchCode = param.split("=")[1];
                }
            }

            if (!twitchCode.isEmpty()) {
                // Exchange the authorization code for an access token
                String clientId = "";
                String clientSecret = "";
                String redirectUri = "http://localhost:3000";
                String tokenUrl = "https://id.twitch.tv/oauth2/token";

                URL url = new URL(tokenUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String postData = String.format(
                        "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                        clientId, clientSecret, twitchCode, redirectUri
                );

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(postData.getBytes());
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String responseLine;
                        StringBuilder responseBuilder = new StringBuilder();
                        while ((responseLine = responseReader.readLine()) != null) {
                            responseBuilder.append(responseLine);
                        }

                        String response = responseBuilder.toString();
                        JSONObject json = new JSONObject(response);
                        String accessToken = json.getString("access_token");

                        // Save the access token for later use
                        SimplexityFileHandler.createTwitchFile(accessToken);

                        String successResponse = "<h1>Authorization successful! You can close this window.</h1>";
                        exchange.sendResponseHeaders(200, successResponse.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(successResponse.getBytes());
                        os.close();
                    }
                } else {
                    String errorResponse = "<h1>Authorization failed!</h1>";
                    exchange.sendResponseHeaders(500, errorResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(errorResponse.getBytes());
                    os.close();
                }
            } else {
                String errorResponse = "<h1>No authorization code found!</h1>";
                exchange.sendResponseHeaders(400, errorResponse.length());
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
            }
        } catch (IOException | JSONException e) {
            System.out.println(Errors.CAUGHT_EXCEPTION.replace("%error%", e.getMessage()));
        }
    }
}
