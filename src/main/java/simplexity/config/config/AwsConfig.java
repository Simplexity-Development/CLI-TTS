package simplexity.config.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.VoiceId;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.AbstractConfig;
import simplexity.messages.Errors;
import simplexity.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AwsConfig extends AbstractConfig {

    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);
    private HashMap<String, VoiceId> voicePrefixes = new HashMap<>();
    private Region awsRegion;
    private VoiceId defaultVoice;
    private String awsAccessID, awsSecretKey;
    private static AwsConfig instance;

    public AwsConfig() {
        super("aws-config.conf", "configs");
        Util.log(logger, "Initializing AWS config class", Level.INFO);


    }

    public static AwsConfig getInstance() {
        if (instance == null) {
            instance = new AwsConfig();
            Util.log(logger, "Generating new instance of AwsConfig", Level.INFO);
        }
        return instance;
    }


    @Override
    public void createDefaultConfig(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            Util.log(logger, "Writing default AWS configuration", Level.INFO);
            writer.write(ConfigDefaults.AWS_ACCESS_KEY);
            writer.write(ConfigDefaults.AWS_SECRET_KEY);
            writer.write(ConfigDefaults.AWS_REGION);
            writer.write(ConfigDefaults.DEFAULT_VOICE);
            writer.write(ConfigDefaults.VOICE_PREFIXES);
        } catch (IOException exception) {
            Util.logAndPrint(logger, "Failed to create default Replace Text Config: " + exception.getMessage(), Level.INFO);
        }

    }

    @Override
    public void reloadConfig() {
        Util.log(logger, "Reloading voice prefixes configuration", Level.INFO);
        reloadVoicePrefixes(getConfig());
        Util.log(logger, "Reloading default voice configuration", Level.INFO);
        reloadDefaultVoice(getConfig());
        Util.log(logger, "Reloading AWS credentials configuration", Level.INFO);
        reloadCredentials(getConfig());
        Util.log(logger, "Reloading AWS region configuration", Level.INFO);
        reloadRegion(getConfig());

    }

    private void reloadVoicePrefixes(Config config) {
        if (voicePrefixes == null) {
            voicePrefixes = new HashMap<>();
        }
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

    private void reloadDefaultVoice(Config config) {
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

    private void reloadCredentials(Config config) {
        awsAccessID = config.getString("aws-access-id");
        awsSecretKey = config.getString("aws-secret-key");
        Util.log(logger, "AWS credentials loaded successfully", Level.INFO);

    }

    public String getAwsAccessID() {
        return awsAccessID;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
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
}
