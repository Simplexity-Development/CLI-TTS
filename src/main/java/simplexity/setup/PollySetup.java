package simplexity.setup;

import com.amazonaws.regions.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.Main;
import simplexity.amazon.PollyHandler;
import simplexity.amazon.SpeechHandler;
import simplexity.config.TTSConfig;
import simplexity.messages.Errors;
import simplexity.messages.Output;
import simplexity.util.Util;

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
        String awsAccessID = TTSConfig.getInstance().getAwsAccessID();
        String awsSecretKey = TTSConfig.getInstance().getAwsSecretKey();
        Region awsRegion = TTSConfig.getInstance().getAwsRegion();
        if (awsAccessID.isEmpty() || awsSecretKey.isEmpty() || awsRegion == null) {
            System.out.println(Errors.NULL_AWS_CREDENTIALS);
            return null;
        }
        try {
            pollyHandler = new PollyHandler(awsAccessID, awsSecretKey, awsRegion);
        } catch (IllegalArgumentException e) {
            System.out.println(Errors.NULL_AWS_CREDENTIALS);
        }
        return pollyHandler;
    }

    public static void connectToPolly(){
        while (true) {
            Main.setPollyHandler(createPollyHandler());
            if (Main.getPollyHandler() != null) {
                return;
            }
            Util.logAndPrint(logger, Output.PLEASE_SAVE_AWS_INFO_IN_CONFIG, Level.INFO);
            scanner.nextLine();
            TTSConfig.getInstance().reloadConfig();
            if (Main.getPollyHandler() != null) {
                return;
            }
        }
    }
}
