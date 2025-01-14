package simplexity.messages;

import simplexity.util.ConsoleColor;

public class Output {
    public static final String HELP_HEADER =
            ConsoleColor.CYAN_BRIGHT + "CLI Text To Speech" + ConsoleColor.RESET;
    public static final String HELP_COMMAND_MESSAGE =
            ConsoleColor.BLUE + "%command_name% " + ConsoleColor.RESET
            + ConsoleColor.WHITE + "- " + ConsoleColor.RESET + ConsoleColor.BLUE_BRIGHT + "%command_description%"
            + ConsoleColor.RESET;
    public static final String RELOAD_MESSAGE =
            ConsoleColor.GREEN + "Config reloaded" + ConsoleColor.RESET;
    public static final String PLEASE_SAVE_AWS_INFO_IN_CONFIG =
            ConsoleColor.YELLOW_BRIGHT + "Please save your AWS credentials in tts-config.conf, then click enter to continue"
            + ConsoleColor.RESET;
    public static final String SHUTTING_DOWN = ConsoleColor.YELLOW_BRIGHT + "CLI Text To Speech is closing..." + ConsoleColor.RESET;
}
