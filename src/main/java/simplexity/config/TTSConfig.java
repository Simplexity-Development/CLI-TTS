package simplexity.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.VoiceId;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.messages.Errors;
import simplexity.util.Util;

import java.io.File;
import java.util.HashMap;

public class TTSConfig {

    private static final Logger logger = LoggerFactory.getLogger(TTSConfig.class);
    private final HashMap<String, String> replaceText = new HashMap<>();
    private final HashMap<String, VoiceId> voicePrefixes = new HashMap<>();
    private Region awsRegion;
    private VoiceId defaultVoice;
    private String awsAccessID, awsSecretKey, twitchChannel, twitchAppClientId, twitchAppClientSecret, twitchAppRedirectURI;
    private boolean connectToTwitch;
    private String twitchOAuth;
    private TTSConfig(){}
    private static TTSConfig instance;
    public static TTSConfig getInstance(){
        if(instance == null) instance = new TTSConfig();
        return instance;
    }

    public HashMap<String, String> getReplaceText() {
        return replaceText;
    }
    public HashMap<String, VoiceId> getVoicePrefixes() {
        return voicePrefixes;
    }
    public Region getAwsRegion() {
        return awsRegion;
    }

    public VoiceId getDefaultVoice() {
        return defaultVoice;
    }

    public void reloadConfig(){
        Config config = initializeConfig();
        reloadReplaceText(config);
        reloadVoicePrefixes(config);
        reloadRegion(config);
        reloadDefaultVoice(config);
        reloadStrings(config);
        reloadBooleans(config);
        reloadTwitchOAuth();
    }

    public void reloadTwitchOAuth(){
        Config config = initializeTwitchAuth();
        if (config == null) return;
        twitchOAuth = config.getString("twitch-oauth");
    }

    private Config initializeConfig() {
        File configFile = SimplexityFileHandler.getInstance().createOrLoadConfigFile();
        return ConfigFactory.parseFile(configFile).resolve();
    }

    private Config initializeTwitchAuth(){
        File oauthFile = new File("twitch-oauth.conf");
        if(oauthFile.exists()){
            return ConfigFactory.parseFile(oauthFile).resolve();
        }
        return null;
    }

    private void reloadReplaceText(Config config){
        replaceText.clear();
        config.getConfig("replace-text").entrySet().forEach(entry -> {
            replaceText.put(
                    entry.getKey().replace("\"", ""),
                    String.valueOf(entry.getValue().unwrapped()));
        });
    }

    private void reloadVoicePrefixes(Config config) {
        voicePrefixes.clear();
        config.getConfig("voice-prefixes").entrySet().forEach(entry -> {
            try {
                VoiceId voiceId = VoiceId.fromValue(String.valueOf(entry.getValue().unwrapped()));
                voicePrefixes.put(entry.getKey().replace("\"", ""), voiceId);
            } catch (IllegalArgumentException e) {
                Util.logAndPrint(logger, Errors.INVALID_VOICE.replace("%voice%", entry.getValue().unwrapped().toString()), Level.ERROR);
            }
        });
    }

    private void reloadRegion(Config config) {
        String region = config.getString("aws-region");
        try {
            awsRegion = Region.getRegion(Regions.valueOf(region));
        } catch (IllegalArgumentException e) {
            Util.logAndPrint(logger, Errors.INVALID_REGION.replace("%region%", region), Level.ERROR);
            awsRegion = Region.getRegion(Regions.US_EAST_1);
        }
    }

    private void reloadDefaultVoice(Config config){
        String voiceString = config.getString("default-voice");
        try {
            defaultVoice = VoiceId.fromValue(voiceString);
        } catch (IllegalArgumentException e) {
            Util.logAndPrint(logger, Errors.INVALID_DEFAULT_VOICE.replace("%voice%", voiceString), Level.ERROR);
            defaultVoice = VoiceId.Brian;
        }
    }

    private void reloadStrings(Config config){
        awsAccessID = config.getString("aws-access-id");
        awsSecretKey = config.getString("aws-secret-key");
        twitchChannel = config.getString("twitch-channel");
        twitchAppClientId = config.getString("twitch-app-client-id");
        twitchAppClientSecret = config.getString("twitch-app-client-secret");
        twitchAppRedirectURI = config.getString("twitch-app-redirect-uri");

    }

    private void reloadBooleans(Config config){
        connectToTwitch = config.getBoolean("connect-to-twitch");
    }

    public String getAwsAccessID() {
        return awsAccessID;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public String getTwitchChannel() {
        return twitchChannel;
    }

    public boolean isConnectToTwitch() {
        return connectToTwitch;
    }

    public String getTwitchOAuth() {
        return twitchOAuth;
    }

    public String getTwitchAppClientId() {
        return twitchAppClientId;
    }

    public String getTwitchAppClientSecret() {
        return twitchAppClientSecret;
    }

    public String getTwitchAppRedirectURI() {
        return twitchAppRedirectURI;
    }
}
