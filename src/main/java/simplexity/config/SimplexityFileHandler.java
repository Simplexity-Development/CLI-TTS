package simplexity.config;

import simplexity.messages.Errors;

import java.io.File;
import java.io.FileWriter;

public class SimplexityFileHandler {

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
            writer.write(ConfigDefaults.REPLACE_TEXT);
            writer.write(ConfigDefaults.DEFAULT_VOICE);
            writer.write(ConfigDefaults.VOICE_PREFIXES);
        } catch (Exception e){
            System.out.println(Errors.CAUGHT_EXCEPTION.replace("%error%", e.getMessage()));
        }
    }

    public static void createTwitchFile(String authCode){
        File file = new File("twitch-oauth.conf");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(ConfigDefaults.TWITCH_OAUTH.replace("%code%", authCode));
        } catch (Exception e){
            System.out.println(Errors.CAUGHT_EXCEPTION.replace("%error%", e.getMessage()));
        }
    }
}
