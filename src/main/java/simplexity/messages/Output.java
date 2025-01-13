package simplexity.messages;

import simplexity.util.ConsoleColors;

public class Output {
    public static final String HELP_HEADER =
            ConsoleColors.CYAN_BRIGHT + "CLI Text To Speech" + ConsoleColors.RESET;
    public static final String HELP_COMMAND_MESSAGE =
            ConsoleColors.BLUE_BOLD_BRIGHT + "%command_name% " + ConsoleColors.RESET
            + ConsoleColors.WHITE + "- " + ConsoleColors.RESET + ConsoleColors.BLUE_BRIGHT + "%command_description%"
            + ConsoleColors.RESET;
    public static final String RELOAD_MESSAGE =
            ConsoleColors.GREEN_BOLD_BRIGHT + "Config reloaded" + ConsoleColors.RESET;
    public static final String PLEASE_SAVE_AWS_INFO_IN_CONFIG =
            ConsoleColors.YELLOW_BRIGHT + "Please save your AWS credentials in tts-config.conf, then click enter to continue"
            + ConsoleColors.RESET;
    public static final String SHUTTING_DOWN = ConsoleColors.YELLOW_BRIGHT + "CLI Text To Speech is closing..." + ConsoleColors.RESET;
}
