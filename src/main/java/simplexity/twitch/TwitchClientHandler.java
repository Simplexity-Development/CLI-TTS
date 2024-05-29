package simplexity.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.TTSConfig;
import simplexity.httpserver.AuthHandler;
import simplexity.messages.Errors;
import simplexity.util.Util;

public class TwitchClientHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthHandler.class);
    OAuth2Credential oAuth2Credential;
    TwitchClient twitchClient;

    public boolean setupClient() {
        String twitchOAuth = TTSConfig.getInstance().getTwitchOAuth();
        if (twitchOAuth == null || twitchOAuth.isEmpty()) {
            Util.logAndPrint(logger, Errors.NO_OAUTH_PROVIDED, Level.ERROR);
            return false;
        }
        oAuth2Credential = new OAuth2Credential("twitch", twitchOAuth);
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(oAuth2Credential)
                .build();
        return true;
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
