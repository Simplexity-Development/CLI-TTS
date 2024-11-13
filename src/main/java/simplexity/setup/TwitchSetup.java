package simplexity.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.Main;
import simplexity.config.TTSConfig;
import simplexity.httpserver.AuthServer;
import simplexity.messages.Errors;
import simplexity.messages.Output;
import simplexity.twitch.TwitchClientHandler;
import simplexity.util.Util;

import java.util.Scanner;

public class TwitchSetup {

    private static final Logger logger = LoggerFactory.getLogger(TwitchSetup.class);
    private static final Scanner scanner = Main.getScanner();

    public static void setup() {
        if (!checkConfig()) {
            return;
        }
        checkApplicationID();
        if (authExists()) {
            return;
        }
        requestAuth();
        Main.setConnectToTwitch(TTSConfig.getInstance().isConnectToTwitch());
        if (Main.isConnectToTwitch()) {
            Main.setTwitchClientHandler(new TwitchClientHandler());
            Main.getTwitchClientHandler().setupClient();
        }
    }

    public static boolean checkConfig() {
        String twitchChannel;
        boolean useTwitch = TTSConfig.getInstance().isConnectToTwitch();
        if (!useTwitch) {
            Util.logAndPrint(logger, Output.YOU_HAVE_DISABLED_TWITCH, Level.INFO);
            return false;
        }
        while (true) {
            twitchChannel = TTSConfig.getInstance().getTwitchChannel();
            boolean useTwitchChannel = TTSConfig.getInstance().isConnectToTwitch();
            if (!useTwitchChannel) {
                return false;
            }
            if (twitchChannel == null || twitchChannel.isBlank() || twitchChannel.isEmpty()) {
                Util.logAndPrint(logger, Errors.NO_CHANNEL_PROVIDED, Level.ERROR);
                Util.logAndPrint(logger, Output.PLEASE_SAVE_CHANNEL_TO_CONFIG, Level.INFO);
                scanner.nextLine();
                TTSConfig.getInstance().reloadConfig();
            } else {
                return true;
            }
        }
    }

    public static void checkApplicationID() {
        String twitchApplicationClientID;
        String twitchApplicationClientSecret;
        while (true) {
            twitchApplicationClientID = TTSConfig.getInstance().getTwitchAppClientId();
            twitchApplicationClientSecret = TTSConfig.getInstance().getTwitchAppClientSecret();
            if (twitchApplicationClientID == null || twitchApplicationClientID.isEmpty() || twitchApplicationClientSecret == null || twitchApplicationClientSecret.isEmpty()) {
                Util.logAndPrint(logger, Errors.NO_APPLICATION_ID_PROVIDED, Level.ERROR);
                Util.logAndPrint(logger, Output.PLEASE_SAVE_IDS_TO_CONFIG, Level.INFO);
                scanner.nextLine();
                TTSConfig.getInstance().reloadConfig();
            } else {
                return;
            }
        }
    }

    public static boolean authExists() {
        String twitchOAuth = TTSConfig.getInstance().getTwitchOAuth();
        if (twitchOAuth == null || twitchOAuth.isEmpty()) {
            logger.info(Errors.NO_OAUTH_PROVIDED);
            return false;
        }
        return true;
    }

    public static void requestAuth() {
        Util.logAndPrint(logger, Errors.NO_OAUTH_PROVIDED, Level.ERROR);
        Util.logAndPrint(logger, Output.WANT_TO_AUTH_MESSAGE, Level.INFO);
        String input = Main.scanner.nextLine();
        while (true) {
            switch (input.toLowerCase()) {
                case "y":
                    authenticate();
                    return;
                case "n":
                    Util.logAndPrint(logger, Output.SKIPPING_TWITCH_SETUP, Level.INFO);
                    return;
                default:
                    Util.logAndPrint(logger, Errors.INVALID_INPUT, Level.ERROR);
            }
        }
    }

    public static void authenticate() {
        while (true) {
            if (!authExists()) {
                printAndRun();
                System.out.println(Output.PRESS_ENTER_TO_CONTINUE);
                Main.scanner.nextLine();
                logger.info("awaiting user input to continue..");
                TTSConfig.getInstance().reloadConfig();
                logger.info("Reloaded config file");
                AuthServer.stop();
                logger.info("Auth server has been stopped");
            } else {
                Util.logAndPrint(logger, Output.AUTH_SUCCESS, Level.INFO);
                return;
            }
        }

    }

    public static void printAndRun(){
        Util.logAndPrint(logger, Output.OPEN_LINK_MESSAGE, Level.INFO);
        Util.logAndPrint(logger, Output.TWITCH_AUTH_URL, Level.INFO);
        AuthServer.run();
    }
}
