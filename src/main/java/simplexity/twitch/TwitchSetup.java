package simplexity.twitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.Main;
import simplexity.config.TTSConfig;
import simplexity.httpserver.AuthServer;
import simplexity.messages.Errors;
import simplexity.messages.Output;
import simplexity.util.Util;

public class TwitchSetup {

    private static final Logger logger = LoggerFactory.getLogger(TwitchSetup.class);

    public void setup() {
        if (!checkConfig()) return;
        if (!checkAuth()){
            if (!requestAuth()) return;
            if (!checkAuth()) {
                System.out.println(Output.PRESS_ENTER_TO_CONTINUE);
                Main.scanner.nextLine();
                logger.info("awaiting user input to continue..");
                TTSConfig.getInstance().reloadConfig();
                logger.info("Reloaded config file");
                AuthServer.stop();
                logger.info("Auth server has been stopped");
            }
            if (checkAuth()) {
                Util.logAndPrint(logger, Output.AUTH_SUCCESS, Level.INFO);
            }
        }
    }

    public boolean checkConfig() {
        String twitchChannel = TTSConfig.getInstance().getTwitchChannel();
        boolean useTwitch = TTSConfig.getInstance().isConnectToTwitch();
        if (!useTwitch) return false;
        if (twitchChannel.isEmpty()) {
            Util.logAndPrint(logger, Errors.NO_CHANNEL_PROVIDED, Level.ERROR);
            return false;
        }
        return true;
    }

    public boolean checkAuth() {
        String twitchOAuth = TTSConfig.getInstance().getTwitchOAuth();
        if (twitchOAuth == null || twitchOAuth.isEmpty()) {
            logger.info(Errors.NO_OAUTH_PROVIDED);
            return false;
        }
        return true;
    }

    public boolean requestAuth() {
        Util.logAndPrint(logger, Errors.NO_OAUTH_PROVIDED, Level.ERROR);
        Util.logAndPrint(logger, Output.WANT_TO_AUTH_MESSAGE, Level.INFO);
        String input = Main.scanner.nextLine();
        while (true) {
            switch (input.toLowerCase()) {
                case "y":
                    Util.logAndPrint(logger, Output.OPEN_LINK_MESSAGE, Level.INFO);
                    Util.logAndPrint(logger, Output.TWITCH_AUTH_URL, Level.INFO);
                    AuthServer.run();
                    return true;
                case "n":
                    Util.logAndPrint(logger, Output.SKIPPING_TWITCH_SETUP, Level.INFO);
                    return false;
                default:
                    Util.logAndPrint(logger, Errors.INVALID_INPUT, Level.ERROR);
            }
        }
    }
}
