package simplexity.config;

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
    public File createOrLoadFile(){
        File file = new File("tts-config.conf");
        if (file.exists()){
            return file;
        }
        createFile();
        return file;
    }

    private void createFile(){
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
            e.printStackTrace();
        }
    }
}
