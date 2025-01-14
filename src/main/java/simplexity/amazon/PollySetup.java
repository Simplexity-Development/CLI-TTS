package simplexity.amazon;

import com.amazonaws.regions.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.Main;
import simplexity.config.config.AwsConfig;
import simplexity.config.config.TTSConfig;
import simplexity.config.locale.Message;
import simplexity.util.Logging;

import java.util.Scanner;

public class PollySetup {
    private static final Logger logger = LoggerFactory.getLogger(PollySetup.class);
    private static final Scanner scanner = Main.getScanner();

    public static void setupPollyAndSpeech(){
        connectToPolly();
        Main.setSpeechHandler(new SpeechHandler());
    }
    public static PollyHandler createPollyHandler() {
        PollyHandler pollyHandler = null;
        String awsAccessID = AwsConfig.getInstance().getAwsAccessID();
        String awsSecretKey = AwsConfig.getInstance().getAwsSecretKey();
        Region awsRegion = AwsConfig.getInstance().getAwsRegion();
        if (awsAccessID.isEmpty() || awsSecretKey.isEmpty() || awsRegion == null) {
            System.out.println(Message.NULL_AWS_CREDENTIALS);
            return null;
        }
        try {
            pollyHandler = new PollyHandler(awsAccessID, awsSecretKey, awsRegion);
        } catch (IllegalArgumentException e) {
            System.out.println(Message.NULL_AWS_CREDENTIALS);
        }
        return pollyHandler;
    }

    public static void connectToPolly(){
        while (true) {
            Main.setPollyHandler(createPollyHandler());
            if (Main.getPollyHandler() != null) {
                return;
            }
            Logging.logAndPrint(logger, Message.PLEASE_SAVE_AWS_INFO_IN_CONFIG.getMessage(), Level.INFO);
            scanner.nextLine();
            TTSConfig.getInstance().reloadConfig();
            if (Main.getPollyHandler() != null) {
                return;
            }
        }
    }
}
