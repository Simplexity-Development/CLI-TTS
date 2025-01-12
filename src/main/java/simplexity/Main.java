package simplexity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplexity.amazon.PollyHandler;
import simplexity.amazon.SpeechHandler;
import simplexity.commands.CommandManager;
import simplexity.commands.ExitCommand;
import simplexity.commands.HelpCommand;
import simplexity.commands.ReloadCommand;
import simplexity.config.TTSConfig;
import simplexity.httpserver.LocalServer;
import simplexity.setup.PollySetup;

import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static CommandManager commandManager;
    public static PollyHandler pollyHandler;
    private static SpeechHandler speechHandler;
    public static Scanner scanner;

    public static void main(String[] args) {
        logger.info("Starting application");
        scanner = new Scanner(System.in);
        commandManager = new CommandManager();
        registerCommands(commandManager);
        TTSConfig.getInstance().reloadConfig();
        PollySetup.setupPollyAndSpeech();
        LocalServer.run();
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("--exit")) {
                return;
            }
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

}