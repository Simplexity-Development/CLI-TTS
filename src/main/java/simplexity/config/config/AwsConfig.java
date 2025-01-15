package simplexity.config.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.VoiceId;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.AbstractConfig;
import simplexity.config.locale.Message;
import simplexity.util.Logging;

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
        super(); //todo figure out a better way to handle the config names
        Logging.log(logger, "Initializing AWS config class", Level.INFO);
    }

    public static AwsConfig getInstance() {
        if (instance == null) {
            instance = new AwsConfig();
            Logging.log(logger, "Generating new instance of AwsConfig", Level.INFO);
        }
        return instance;
    }


    @Override
    public void createDefaultConfig(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            Logging.log(logger, "Writing default AWS configuration", Level.INFO);
            writer.write(AwsConfigDefaults.AWS_ACCESS_KEY);
            writer.write(AwsConfigDefaults.AWS_SECRET_KEY);
            writer.write(AwsConfigDefaults.AWS_REGION);
            writer.write(AwsConfigDefaults.DEFAULT_VOICE);
            writer.write(AwsConfigDefaults.VOICE_PREFIXES);
        } catch (IOException exception) {
            Logging.logAndPrint(logger, "Failed to create default Replace Text Config: " + exception.getMessage(), Level.INFO);
        }

    }

    @Override
    public void reloadConfig() {
        loadConfig("aws-config.conf", "configs");
        Logging.log(logger, "Reloading voice prefixes configuration", Level.INFO);
        reloadVoicePrefixes(getConfig());
        Logging.log(logger, "Reloading default voice configuration", Level.INFO);
        reloadDefaultVoice(getConfig());
        Logging.log(logger, "Reloading AWS credentials configuration", Level.INFO);
        reloadCredentials(getConfig());
        Logging.log(logger, "Reloading AWS region configuration", Level.INFO);
        reloadRegion(getConfig());

    }

    private void reloadVoicePrefixes(Config config) {
        if (voicePrefixes == null) {
            voicePrefixes = new HashMap<>();
        }
        voicePrefixes.clear();
        Logging.log(logger, "Clearing existing voice prefix entries", Level.DEBUG);
        config.getConfig("voice-prefixes").entrySet().forEach(entry -> {
            try {
                VoiceId voiceId = VoiceId.fromValue(String.valueOf(entry.getValue().unwrapped()));
                voicePrefixes.put(entry.getKey().replace("\"", ""), voiceId);
            } catch (IllegalArgumentException e) {
                Logging.logAndPrint(logger, Message.INVALID_VOICE.getMessage().replace("%voice%", entry.getValue().unwrapped().toString()), Level.ERROR);
            }
        });
        Logging.log(logger, "Voice prefixes configuration reloaded successfully", Level.INFO);
    }

    private void reloadRegion(Config config) {
        String region = config.getString("aws-region");
        try {
            awsRegion = Region.getRegion(Regions.valueOf(region));
            Logging.log(logger, "AWS region set to: " + region, Level.INFO);
        } catch (IllegalArgumentException e) {
            Logging.logAndPrint(logger, Message.INVALID_REGION.getMessage().replace("%region%", region), Level.ERROR);
            awsRegion = Region.getRegion(Regions.US_EAST_1);
            Logging.log(logger, "AWS region defaulted to US_EAST_1", Level.WARN);
        }
    }

    private void reloadDefaultVoice(Config config) {
        String voiceString = config.getString("default-voice");
        try {
            defaultVoice = VoiceId.fromValue(voiceString);
            Logging.log(logger, "Default voice set to: " + voiceString, Level.INFO);
        } catch (IllegalArgumentException e) {
            Logging.logAndPrint(logger, Message.INVALID_VOICE.getMessage().replace("%voice%", voiceString), Level.ERROR);
            defaultVoice = VoiceId.Brian;
            Logging.log(logger, "Default voice set to fallback: Brian", Level.WARN);
        }
    }

    private void reloadCredentials(Config config) {
        awsAccessID = config.getString("aws-access-id");
        awsSecretKey = config.getString("aws-secret-key");
        Logging.log(logger, "AWS credentials loaded successfully", Level.INFO);

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
