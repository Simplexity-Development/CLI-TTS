package simplexity.messages;

import simplexity.util.ConsoleColors;

public class Output {
    public static final String HELP_HEADER = ConsoleColors.CYAN_BRIGHT + "CLI Text To Speech" + ConsoleColors.RESET;
    public static final String HELP_COMMAND_MESSAGE = ConsoleColors.BLUE_BOLD_BRIGHT + "%command_name% " + ConsoleColors.RESET
            + ConsoleColors.WHITE + "- " + ConsoleColors.RESET + ConsoleColors.BLUE_BRIGHT + "%command_description%"
            + ConsoleColors.RESET;
    public static final String RELOAD_MESSAGE = ConsoleColors.GREEN_BOLD_BRIGHT + "Config reloaded" + ConsoleColors.RESET;
    public static final String TWITCH_AUTH_URL = "https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=r0ret1izmdz1alwyq98d2lgjyo0h5r&redirect_uri=http://localhost:3000&scope=chat%3Aread+chat%3Aedit";
}
