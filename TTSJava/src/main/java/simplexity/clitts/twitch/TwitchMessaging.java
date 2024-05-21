package simplexity.clitts.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import simplexity.clitts.TTSConfig;

public class TwitchMessaging {
    private TwitchMessaging(){}
    private static TwitchMessaging instance;
    public static TwitchMessaging getInstance(){
        if(instance == null) instance = new TwitchMessaging();
        return instance;
    }
    OAuth2Credential credential;
    TwitchClient twitchClient;

    public void setupTwitch(String authorization){
        credential = new OAuth2Credential("twitch", authorization);
        twitchClient = TwitchClientBuilder.builder()
                .withChatAccount(credential)
                .withEnableChat(true)
                .build();
        twitchClient.getChat().joinChannel(TTSConfig.getInstance().getTwitchChannel());
        twitchClient.getChat().sendMessage(TTSConfig.getInstance().getTwitchChannel(), "Hello world!");
    }

}
