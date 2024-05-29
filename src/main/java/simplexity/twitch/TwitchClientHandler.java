package simplexity.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import simplexity.config.TTSConfig;
import simplexity.messages.Errors;

public class TwitchClientHandler {

    OAuth2Credential oAuth2Credential;
    TwitchClient twitchClient;

    public boolean setupClient() {
        String twitchOAuth = TTSConfig.getInstance().getTwitchOAuth();
        if (twitchOAuth == null || twitchOAuth.isEmpty()) {
            System.out.println(Errors.NO_OAUTH_PROVIDED);
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
