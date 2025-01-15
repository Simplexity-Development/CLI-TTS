package simplexity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.amazon.PollyHandler;
import simplexity.amazon.SpeechHandler;
import simplexity.commands.CommandManager;
import simplexity.commands.ExitCommand;
import simplexity.commands.HelpCommand;
import simplexity.commands.ReloadCommand;
import simplexity.config.AbstractConfig;
import simplexity.config.config.AwsConfig;
import simplexity.config.config.ReplaceTextConfig;
import simplexity.config.config.TtsConfig;
import simplexity.config.locale.LocaleConfig;
import simplexity.httpserver.LocalServer;
import simplexity.amazon.PollySetup;
import simplexity.util.Logging;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static CommandManager commandManager;
    private static final ArrayList<AbstractConfig> configs = new ArrayList<>();
    public static PollyHandler pollyHandler;
    private static SpeechHandler speechHandler;
    public static Scanner scanner;
    public static boolean runApp = true;

    public static void main(String[] args) {
        Logging.log(logger, "Starting application", Level.INFO);
        scanner = new Scanner(System.in);
        commandManager = new CommandManager();
        registerCommands(commandManager);
        setupConfigs();
        PollySetup.setupPollyAndSpeech();
        LocalServer.run();
        while (runApp) {
            String input = scanner.nextLine();
            if (!commandManager.runCommand(input)) {
                speechHandler.processSpeech(input);
            }
        }
    }

    private static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(new HelpCommand("--help", "Displays the help messages"));
        commandManager.registerCommand(new ExitCommand("--exit", "Terminates the program"));
        commandManager.registerCommand(new ReloadCommand("--reload", "Reloads the configuration"));
    }

    private static void setupConfigs(){
        AwsConfig awsConfig = new AwsConfig();
        configs.add(awsConfig);
        TtsConfig ttsConfig = new TtsConfig();
        configs.add(ttsConfig);
        ReplaceTextConfig replaceTextConfig = new ReplaceTextConfig();
        configs.add(replaceTextConfig);
        LocaleConfig localeConfig = new LocaleConfig();
        configs.add(localeConfig);
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static PollyHandler getPollyHandler() {
        return pollyHandler;
    }

    public static void setSpeechHandler(SpeechHandler speechHandlerToSet){
        speechHandler = speechHandlerToSet;
    }

    public static void setPollyHandler(PollyHandler pollyHandlerToSet){
        pollyHandler = pollyHandlerToSet;
    }

    public static Scanner getScanner(){
        return scanner;
    }

    public static ArrayList<AbstractConfig> getConfigs() {
        return configs;
    }

}