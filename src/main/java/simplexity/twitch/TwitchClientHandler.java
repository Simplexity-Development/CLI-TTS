package simplexity.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import simplexity.config.TTSConfig;

public class TwitchClientHandler {

    OAuth2Credential oAuth2Credential;
    TwitchClient twitchClient;

    public boolean setupClient(){
        String twitchOAuth = TTSConfig.getInstance().getTwitchOAuth();
        if (twitchOAuth == null) {
            System.out.println("TWITCH OAUTH IS NULL");
            return false;
        }
        oAuth2Credential = new OAuth2Credential("twitch", TTSConfig.getInstance().getTwitchOAuth());
        twitchClient =  TwitchClientBuilder.builder().withEnableChat(true).withChatAccount(oAuth2Credential).build();
        return true;
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }




}
