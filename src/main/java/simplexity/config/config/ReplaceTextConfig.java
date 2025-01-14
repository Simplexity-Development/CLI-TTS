package simplexity.config.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.AbstractConfig;
import simplexity.util.Logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ReplaceTextConfig extends AbstractConfig {

    private static final Logger logger = LoggerFactory.getLogger(ReplaceTextConfig.class);
    private HashMap<String, String> replaceText = new HashMap<>();
    private static ReplaceTextConfig instance;
    public ReplaceTextConfig() {
        super("text-replace.conf", "configs");
        Logging.log(logger, "Initializing Replace Text config class", Level.INFO);
    }

    public static ReplaceTextConfig getInstance() {
        if (instance == null) {
            instance = new ReplaceTextConfig();
            Logging.log(logger, "Creating new ReplaceTextConfig instance", Level.INFO);
        }
        return instance;
    }

    @Override
    public void createDefaultConfig(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            Logging.log(logger, "Writing default replace text configuration", Level.INFO);
            writer.write(ConfigDefaults.REPLACE_TEXT);
        } catch (IOException exception) {
            Logging.logAndPrint(logger, "Failed to create default Replace Text Config: " + exception.getMessage(), Level.INFO);
        }
    }

    @Override
    public void reloadConfig() {
        if (replaceText == null) {
            replaceText = new HashMap<>();
        }
        replaceText.clear();
        Logging.log(logger, "Clearing existing replace text entries", Level.DEBUG);
        config.getConfig("replace-text").entrySet().forEach(entry -> {
            replaceText.put(
                    entry.getKey().replace("\"", ""),
                    String.valueOf(entry.getValue().unwrapped()));
        });
        Logging.log(logger, "Replace text configuration reloaded successfully", Level.INFO);
    }

    public HashMap<String, String> getReplaceText() {
        return replaceText;
    }
}
