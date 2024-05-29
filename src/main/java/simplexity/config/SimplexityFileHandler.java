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
            instance = new SimplexityFileHandler();
        }
        return instance;
    }
    public File createOrLoadConfigFile(){
        File file = new File("tts-config.conf");
        if (file.exists()){
            return file;
        }
        createConfigFile();
        return file;
    }

    private void createConfigFile(){
        File file = new File("tts-config.conf");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(ConfigDefaults.AWS_REGION);
            writer.write(ConfigDefaults.AWS_ACCESS_KEY);
            writer.write(ConfigDefaults.AWS_SECRET_KEY);
            writer.write(ConfigDefaults.CONNECT_TO_TWITCH);
            writer.write(ConfigDefaults.TWITCH_CHANNEL);
            writer.write(ConfigDefaults.TWITCH_APP_CLIENT_ID);
            writer.write(ConfigDefaults.TWITCH_APP_CLIENT_SECRET);
            writer.write(ConfigDefaults.REPLACE_TEXT);
            writer.write(ConfigDefaults.DEFAULT_VOICE);
            writer.write(ConfigDefaults.VOICE_PREFIXES);
        } catch (Exception exception){
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.ERROR);
        }
    }

    public static void createTwitchFile(String accessToken){
        File file = new File("twitch-oauth.conf");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("twitch-oauth=\"" + accessToken + "\"");
        } catch (Exception exception){
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.ERROR);
        }
    }
}
