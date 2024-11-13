package simplexity.httpserver;

import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplexity.config.TTSConfig;

import java.io.IOException;
import java.io.OutputStream;

public class AuthHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthHandler.class);
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
                String clientID = TTSConfig.getInstance().getTwitchAppClientId();
                String clientSecret = TTSConfig.getInstance().getTwitchAppClientSecret();
                String redirectUri = TTSConfig.getInstance().getTwitchAppRedirectURI();
                TwitchIdentityProvider identityProvider = new TwitchIdentityProvider(clientID, clientSecret, redirectUri);
                if (clientID == null || clientSecret == null || redirectUri == null ||
                        clientID.isEmpty() || clientSecret.isEmpty() || redirectUri.isEmpty()) {
                    logger.error("Twitch app credentials not set");
                    String errorResponse = "<h1>Twitch App Credentials not set</h1>";
                    exchange.sendResponseHeaders(500, errorResponse.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(errorResponse.getBytes());
                    }
                    return;
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
