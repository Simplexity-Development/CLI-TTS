package simplexity.messages;

import simplexity.util.ConsoleColors;

public class Errors {
    public static final String INVALID_VOICE =
            ConsoleColors.RED_BRIGHT +
            """
            Error: '%voice%' is not a valid voice.
            Please make sure you are only choosing from standard voices.
            Standard voices can be found here:
            https://docs.aws.amazon.com/polly/latest/dg/voicelist.html
            """ + ConsoleColors.RESET;

    public static final String INVALID_REGION =
            ConsoleColors.RED_BRIGHT +
            """
            Error: '%region%' is not a valid text.
            Regions can be found here:
            https://aws.amazon.com/about-aws/global-infrastructure/regions_az/
            Using default region of 'US_EAST_1'
            """ + ConsoleColors.RESET;

    public static final String INVALID_DEFAULT_VOICE =
            ConsoleColors.RED_BRIGHT +
            """
            Error: '%voice%' is not a valid voice.
            Please make sure you are only choosing from standard voices.
            Standard voices can be found here:
            https://docs.aws.amazon.com/polly/latest/dg/voicelist.html
            Setting default voice to 'Brian'
            """ + ConsoleColors.RESET;

    public static final String UNKNOWN_COMMAND =
            ConsoleColors.RED_BRIGHT + """
            ERROR: '%command%' is not a recognized command
            """ + ConsoleColors.RESET;

    public static final String NULL_AWS_CREDENTIALS =
            ConsoleColors.RED_BRIGHT + """
            ERROR: AWS credentials are null, please fill them out
            """ + ConsoleColors.RESET;

    public static final String CAUGHT_EXCEPTION =
            ConsoleColors.RED_BRIGHT + """
            ERROR: %error%
            """ + ConsoleColors.RESET;

    public static final String TWITCH_AUTH_NULL =
            ConsoleColors.RED_BRIGHT + """
            ERROR: Twitch auth is null
            """ + ConsoleColors.RESET;

    public static final String NO_CHANNEL_PROVIDED =
            ConsoleColors.RED_BRIGHT + """
            ERROR: You have set 'connect-to-twitch' to true but have not provided a channel to connect to.
            """ + ConsoleColors.RESET;

    public static final String NO_OAUTH_PROVIDED =
            ConsoleColors.RED_BRIGHT + """
            ERROR: You have set 'connect-to-twitch' to true but have not authorized this application to connect to twitch.
            """ + ConsoleColors.RESET;

    public static final String INVALID_INPUT =
            ConsoleColors.RED_BRIGHT + """
            ERROR: Invalid input
            """ + ConsoleColors.RESET;

    public static final String NO_APPLICATION_ID_PROVIDED =
            ConsoleColors.RED_BRIGHT + """
            ERROR: You have set 'connect-to-twitch' to true but have not provided an application ID/application Secret.
            """ + ConsoleColors.RESET;


}
