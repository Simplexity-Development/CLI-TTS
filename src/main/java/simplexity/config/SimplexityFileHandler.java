package simplexity.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.messages.Errors;
import simplexity.util.Util;

import java.io.File;
import java.io.FileWriter;

public class SimplexityFileHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimplexityFileHandler.class);
    private SimplexityFileHandler(){}
    private static SimplexityFileHandler instance;
    public static SimplexityFileHandler getInstance(){
        if(instance == null){
            Util.log(logger, "Creating a new instance of SimplexityFileHandler", Level.INFO);
            instance = new SimplexityFileHandler();
        }
        Util.log(logger, "Returning existing instance of SimplexityFileHandler", Level.INFO);
        return instance;
    }
    public File createOrLoadConfigFile(){
        Util.log(logger, "Attempting to create or load the config file: tts-config.conf", Level.INFO);
        File file = new File("tts-config.conf");
        if (file.exists()){
            Util.log(logger, "Config file already exists. Returning existing file.", Level.INFO);
            return file;
        }
        Util.log(logger, "Config file does not exist. Creating a new config file.", Level.WARN);
        createConfigFile();
        return file;
    }

    private void createConfigFile(){
        File file = new File("tts-config.conf");
        try (FileWriter writer = new FileWriter(file)) {
            Util.log(logger, "Writing default configuration values to tts-config.conf", Level.INFO);
            writer.write(ConfigDefaults.AWS_REGION);
            writer.write(ConfigDefaults.AWS_ACCESS_KEY);
            writer.write(ConfigDefaults.AWS_SECRET_KEY);
            writer.write(ConfigDefaults.REPLACE_TEXT);
            writer.write(ConfigDefaults.DEFAULT_VOICE);
            writer.write(ConfigDefaults.VOICE_PREFIXES);
            writer.write(ConfigDefaults.SERVER_PORT);
            Util.log(logger, "Successfully created and wrote to tts-config.conf", Level.INFO);
        } catch (Exception exception){
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.ERROR);
        }
    }
}
