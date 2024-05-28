package simplexity.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import simplexity.config.SimplexityFileHandler;
import simplexity.messages.Errors;

import java.io.IOException;
import java.io.OutputStream;

public class AuthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String twitchCode = exchange.getRequestURI().getQuery().split("=")[1];
            twitchCode = twitchCode.split("&")[0];
            System.out.println(twitchCode);
            SimplexityFileHandler.createTwitchFile(twitchCode);
            String response = "<h1>HI</h1>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            System.out.println(Errors.CAUGHT_EXCEPTION.replace("%error%", e.getMessage()));
        }
    }
}
