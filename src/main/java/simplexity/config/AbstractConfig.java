package simplexity.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.util.Util;

import java.io.File;

public abstract class AbstractConfig {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);
    protected Config config;

    public AbstractConfig(String configPath, String folderPath) {
        loadConfig(configPath, folderPath);
        reloadConfig();
    }

    private void loadConfig(String configPath, String folderPath) {
        Util.log(logger, "Loading configuration from directory: " + folderPath + ", file:" + configPath, Level.INFO);
        File folder = checkForOrCreateFolder(folderPath);
        if (folder == null) {
            Util.logAndPrint(logger, "Unable to continue loading: " + configPath, Level.WARN);
        }
        File configFile = new File(folder, configPath);
        if (!configFile.exists()) {
            Util.log(logger, "Config file does not exist: " + configPath, Level.INFO);
            createDefaultConfig(configFile);
        }
        this.config = ConfigFactory.parseFile(configFile).resolve();
        Util.log(logger, "Loaded configuration from: " + configPath, Level.INFO);
    }

    private File checkForOrCreateFolder(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Util.logAndPrint(logger, "Failed to create directory: " + folderPath, Level.WARN);
                return null;
            } else {
                Util.log(logger, "Successfully created directory: " + folderPath, Level.INFO);
            }
        }
        return directory;
    }

    public abstract void createDefaultConfig(File configFile);

    public abstract void reloadConfig();

    public Config getConfig() {
        return config;
    }
}
