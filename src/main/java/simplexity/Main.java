package simplexity;

import com.amazonaws.regions.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplexity.amazon.PollyHandler;
import simplexity.amazon.SpeechHandler;
import simplexity.commands.CommandManager;
import simplexity.commands.ExitCommand;
import simplexity.commands.HelpCommand;
import simplexity.commands.ReloadCommand;
import simplexity.config.TTSConfig;
import simplexity.messages.Errors;
import simplexity.twitch.TwitchClientHandler;
import simplexity.twitch.TwitchSetup;

import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static CommandManager commandManager;
    private static PollyHandler pollyHandler;
    private static SpeechHandler speechHandler;
    private static TwitchClientHandler twitchClientHandler;
    private static TwitchSetup twitchSetup;
    public static Scanner scanner;

    public static void main(String[] args) {
        logger.info("Starting application");
        scanner = new Scanner(System.in);
        commandManager = new CommandManager();
        registerCommands(commandManager);
        TTSConfig.getInstance().reloadConfig();
        pollyHandler = createPollyHandler();
        speechHandler = new SpeechHandler();
        twitchClientHandler = new TwitchClientHandler();
        twitchSetup = new TwitchSetup();
        twitchSetup.setup();
        twitchClientHandler.setupClient();
        twitchClientHandler.getTwitchClient().getChat().joinChannel("RhythmWeHear");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("--exit")) {
                return;
            }
            if (!commandManager.runCommand(input)) {
                twitchClientHandler.getTwitchClient().getChat().sendMessage("RhythmWeHear", input);

                speechHandler.processSpeech(input);
            } else {
                System.out.println("command executed");
            }
        }
    }

    private static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(new HelpCommand("--help", "Displays the help messages"));
        commandManager.registerCommand(new ExitCommand("--exit", "Terminates the program"));
        commandManager.registerCommand(new ReloadCommand("--reload", "Reloads the configuration"));
    }

    public static CommandManager getCommandManager() {
        return commandManager;
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


    public static PollyHandler getPollyHandler() {
        return pollyHandler;
    }
}