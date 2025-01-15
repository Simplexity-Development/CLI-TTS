package simplexity.config.config;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.AbstractConfig;
import simplexity.util.Logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TtsConfig extends AbstractConfig {

    private static final Logger logger = LoggerFactory.getLogger(TtsConfig.class);


    private int port;

    public TtsConfig() {
        super();
        Logging.log(logger, "Initializing TTS config class", Level.INFO);
    }

    private static TtsConfig instance;

    public static TtsConfig getInstance() {
        if (instance == null) instance = new TtsConfig();
        return instance;
    }

    @Override
    public void createDefaultConfig(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(TtsConfigDefaults.SERVER_PORT);
        } catch (IOException exception) {
            Logging.logAndPrint(logger, "Failed to create default TTS Config: " + exception.getMessage(), Level.INFO);
        }
    }

    @Override
    public void reloadConfig() {
        loadConfig("tts-config.conf", "configs");
        reloadInts(getConfig());
        Logging.log(logger, "TTS configuration reloaded successfully", Level.INFO);
    }


    private void reloadInts(Config config) {
        port = config.getInt("server-port");
        Logging.log(logger, "Server port set to: " + port, Level.INFO);
    }

    public int getPort() {
        return port;
    }
}
