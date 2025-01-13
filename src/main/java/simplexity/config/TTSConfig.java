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
    private String awsAccessID, awsSecretKey;
    private int port;

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
        Util.log(logger, "Reloading TTS configuration", Level.INFO);
        Config config = initializeConfig();
        Util.log(logger, "Reloading replace text configuration", Level.INFO);
        reloadReplaceText(config);
        Util.log(logger, "Reloading voice prefixes configuration", Level.INFO);
        reloadVoicePrefixes(config);
        Util.log(logger, "Reloading AWS region configuration", Level.INFO);
        reloadRegion(config);
        Util.log(logger, "Reloading default voice configuration", Level.INFO);
        reloadDefaultVoice(config);
        Util.log(logger, "Reloading AWS credentials and server port", Level.INFO);
        reloadStrings(config);
        reloadInts(config);
        Util.log(logger, "TTS configuration reloaded successfully", Level.INFO);

    }



    private Config initializeConfig() {
        Util.log(logger, "Initializing configuration file from SimplexityFileHandler", Level.INFO);
        File configFile = SimplexityFileHandler.getInstance().createOrLoadConfigFile();
        Config config =  ConfigFactory.parseFile(configFile).resolve();
        Util.log(logger, "Configuration file parsed successfully", Level.INFO);
        return config;
    }


    private void reloadReplaceText(Config config){
        replaceText.clear();
        Util.log(logger, "Clearing existing replace text entries", Level.DEBUG);
        config.getConfig("replace-text").entrySet().forEach(entry -> {
            replaceText.put(
                    entry.getKey().replace("\"", ""),
                    String.valueOf(entry.getValue().unwrapped()));
        });
        Util.log(logger, "Replace text configuration reloaded successfully", Level.INFO);
    }

    private void reloadVoicePrefixes(Config config) {
        voicePrefixes.clear();
        Util.log(logger, "Clearing existing voice prefix entries", Level.DEBUG);
        config.getConfig("voice-prefixes").entrySet().forEach(entry -> {
            try {
                VoiceId voiceId = VoiceId.fromValue(String.valueOf(entry.getValue().unwrapped()));
                voicePrefixes.put(entry.getKey().replace("\"", ""), voiceId);
            } catch (IllegalArgumentException e) {
                Util.logAndPrint(logger, Errors.INVALID_VOICE.replace("%voice%", entry.getValue().unwrapped().toString()), Level.ERROR);
            }
        });
        Util.log(logger, "Voice prefixes configuration reloaded successfully", Level.INFO);
    }

    private void reloadRegion(Config config) {
        String region = config.getString("aws-region");
        try {
            awsRegion = Region.getRegion(Regions.valueOf(region));
            Util.log(logger, "AWS region set to: " + region, Level.INFO);
        } catch (IllegalArgumentException e) {
            Util.logAndPrint(logger, Errors.INVALID_REGION.replace("%region%", region), Level.ERROR);
            awsRegion = Region.getRegion(Regions.US_EAST_1);
            Util.log(logger, "AWS region defaulted to US_EAST_1", Level.WARN);
        }
    }

    private void reloadDefaultVoice(Config config){
        String voiceString = config.getString("default-voice");
        try {
            defaultVoice = VoiceId.fromValue(voiceString);
            Util.log(logger, "Default voice set to: " + voiceString, Level.INFO);
        } catch (IllegalArgumentException e) {
            Util.logAndPrint(logger, Errors.INVALID_DEFAULT_VOICE.replace("%voice%", voiceString), Level.ERROR);
            defaultVoice = VoiceId.Brian;
            Util.log(logger, "Default voice set to fallback: Brian", Level.WARN);
        }
    }

    private void reloadStrings(Config config){
        awsAccessID = config.getString("aws-access-id");
        awsSecretKey = config.getString("aws-secret-key");
        Util.log(logger, "AWS credentials loaded successfully", Level.INFO);

    }

    private void reloadInts(Config config){
        port = config.getInt("server-port");
        Util.log(logger, "Server port set to: " + port, Level.INFO);
    }


    public String getAwsAccessID() {
        return awsAccessID;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public int getPort() {
        return port;
    }
}
