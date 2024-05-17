package simplexity.clitts.util;

public class Messages {
    public static final String enterText = Colors.yellow + "Enter text:" + Colors.formatReset;
    public static final String separator = "\n====================================";
    public static final String exitMessage = Colors.boldGreen + "Exiting program..." + Colors.formatReset;
    public static final String reloadMessage = Colors.boldGreen + "Reloading configuration..." + Colors.formatReset;
    public static final String reloadHelp = "\nType" + Colors.cyan + " --reload" + Colors.formatReset + " to reload the configuration.";
    public static final String exitHelp = "\nType " + Colors.cyan + "--exit" + Colors.formatReset + " to end the program.";
    public static final String usageHelp = "Type your text then press" + Colors.cyan + " Enter" + Colors.formatReset + " to convert to speech.";
    public static final String helpHelp = "\nType " + Colors.cyan + "--help" + Colors.formatReset + " to see this screen again";
    public static final String secretOrIDNotSet = Colors.boldRed + "AWS Access ID or Access Secret is not set. Please set your AWS Access ID and Access secret then use '--reload'" + Colors.formatReset;
    public static final String startupMessage = usageHelp + reloadHelp + exitHelp + helpHelp + separator;
    public static final String helpMessage = Colors.formatReset + "\n" + startupMessage;

    public static final String needTwitchAuth = Colors.boldRed + "This application does not yet have authorization to interact with twitch" + Colors.formatReset;
    public static final String visitLink = "Visit the following link to authorize this application to send messages in twitch chat: ";
    public static final String twitchAuthLink = "https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=r0ret1izmdz1alwyq98d2lgjyo0h5r&redirect_uri=http://localhost:3000&scope=chat%3Aread+chat%3Aedit";
    public static final String twitchAuthMessage = Colors.boldGreen + "Twitch authorization successful!" + Colors.formatReset;
    public static final String currentlyConnectedTo = Colors.yellow + "You are currently connected to the channel: " + Colors.formatReset + Colors.cyan;
    public static final String stayConnectedPrompt = Colors.formatReset + Colors.yellow + "\nWould you like to stay hooked into the channel? (y/n)" + Colors.formatReset
            + Colors.boldRed + "\nAll messages processed while hooked into twitch will be sent to that channels chat." + Colors.formatReset;
    public static final String stayConnectedMessage = Colors.boldGreen + "Your messages will be relayed to your selected channel" + Colors.formatReset;
    public static final String stayConnectedError = Colors.boldRed + "You must enter y or n" + Colors.formatReset;
    public static final String disconnectMessage = Colors.boldGreen + "You have been disconnected from the channel" + Colors.formatReset;
    public static final String canDisableInConfig =  Colors.boldRed + "You can disable connecting to twitch in the config file" + Colors.formatReset;
    public static final String twitchAuthError = Colors.boldRed + "There was an error with the twitch authorization" + Colors.formatReset;
}
