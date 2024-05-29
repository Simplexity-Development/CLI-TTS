package simplexity.twitch;

import simplexity.Main;
import simplexity.config.TTSConfig;
import simplexity.httpserver.AuthServer;
import simplexity.messages.Errors;
import simplexity.messages.Output;

import java.util.concurrent.CountDownLatch;

public class TwitchSetup {

    private static final CountDownLatch latch = new CountDownLatch(1);

    public void setup() {
        if (!checkConfig()) return;
        if (!checkAuth()){
            if (!requestAuth()) return;
            if (!checkAuth()) {
                System.out.println(Output.PRESS_ENTER_TO_CONTINUE);
                Main.scanner.nextLine();
                TTSConfig.getInstance().reloadConfig();
                AuthServer.stop();
            }
            if (checkAuth()) {
                System.out.println(Output.AUTH_SUCCESS);
            }
        }
    }

    public boolean checkConfig() {
        String twitchChannel = TTSConfig.getInstance().getTwitchChannel();
        boolean useTwitch = TTSConfig.getInstance().isConnectToTwitch();
        if (!useTwitch) return false;
        if (twitchChannel.isEmpty()) {
            System.out.println(Errors.NO_CHANNEL_PROVIDED);
            return false;
        }
        return true;
    }

    public boolean checkAuth() {
        String twitchOAuth = TTSConfig.getInstance().getTwitchOAuth();
        if (twitchOAuth == null || twitchOAuth.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean requestAuth() {
        System.out.println(Errors.NO_OAUTH_PROVIDED);
        System.out.println(Output.WANT_TO_AUTH_MESSAGE);
        String input = Main.scanner.nextLine();
        while (true) {
            switch (input.toLowerCase()) {
                case "y":
                    System.out.println(Output.OPEN_LINK_MESSAGE);
                    System.out.println(Output.TWITCH_AUTH_URL);
                    AuthServer.run();
                    return true;
                case "n":
                    System.out.println(Output.SKIPPING_TWITCH_SETUP);
                    return false;
                default:
                    System.out.println(Errors.INVALID_INPUT);
            }
        }
    }
}
