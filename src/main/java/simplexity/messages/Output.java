package simplexity.messages;

import simplexity.util.ConsoleColors;

public class Output {
    public static final String HELP_HEADER = ConsoleColors.CYAN_BRIGHT + "CLI Text To Speech" + ConsoleColors.RESET;
    public static final String HELP_COMMAND_MESSAGE = ConsoleColors.BLUE_BOLD_BRIGHT + "%command_name% " + ConsoleColors.RESET
            + ConsoleColors.WHITE + "- " + ConsoleColors.RESET + ConsoleColors.BLUE_BRIGHT + "%command_description%"
            + ConsoleColors.RESET;
    public static final String RELOAD_MESSAGE = ConsoleColors.GREEN_BOLD_BRIGHT + "Config reloaded" + ConsoleColors.RESET;
    public static final String WANT_TO_AUTH_MESSAGE = ConsoleColors.YELLOW_BRIGHT + "Do you want to authenticate with Twitch? (y/n) \n" + ConsoleColors.RESET;
    public static final String OPEN_LINK_MESSAGE = ConsoleColors.YELLOW_BRIGHT + "Please open this link in your browser" + ConsoleColors.RESET;
    public static final String TWITCH_AUTH_URL = "\n https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=r0ret1izmdz1alwyq98d2lgjyo0h5r&redirect_uri=http://localhost:3000&scope=chat%3Aread+chat%3Aedit \n";
    public static final String SKIPPING_TWITCH_SETUP = ConsoleColors.YELLOW_BRIGHT + "Skipping Twitch setup.." + ConsoleColors.RESET;
    public static final String PLEASE_SAVE_AWS_INFO_IN_CONFIG = ConsoleColors.YELLOW_BRIGHT + "Please save your AWS credentials in tts-config.conf, then click enter to continue" + ConsoleColors.RESET;
    public static final String PRESS_ENTER_TO_CONTINUE = ConsoleColors.YELLOW_BRIGHT + "Press enter to continue after authorization has been completed" + ConsoleColors.RESET;
    public static final String PLEASE_SAVE_CHANNEL_TO_CONFIG = ConsoleColors.YELLOW_BRIGHT + "Please save your channel name in tts-config.conf, then click enter to continue" + ConsoleColors.RESET;
    public static final String PLEASE_SAVE_IDS_TO_CONFIG = ConsoleColors.YELLOW_BRIGHT + "Please save your Twitch Application IDs in tts-config.conf, then click enter to continue" + ConsoleColors.RESET;
    public static final String YOU_HAVE_DISABLED_TWITCH = ConsoleColors.YELLOW_BRIGHT + "You have disabled Twitch" + ConsoleColors.RESET;
    public static final String AUTH_SUCCESS = ConsoleColors.GREEN_BOLD_BRIGHT + "Authentication successful\n" + ConsoleColors.RESET;
}
