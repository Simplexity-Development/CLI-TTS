package simplexity.messages;

import simplexity.util.ConsoleColor;

public class Errors {
    public static final String INVALID_VOICE =
            ConsoleColor.RED_BRIGHT +
            """
            Error: '%voice%' is not a valid voice.
            Please make sure you are only choosing from standard voices.
            Standard voices can be found here:
            https://docs.aws.amazon.com/polly/latest/dg/voicelist.html
            """ + ConsoleColor.RESET;

    public static final String INVALID_REGION =
            ConsoleColor.RED_BRIGHT +
            """
            Error: '%region%' is not a valid text.
            Regions can be found here:
            https://aws.amazon.com/about-aws/global-infrastructure/regions_az/
            Using default region of 'US_EAST_1'
            """ + ConsoleColor.RESET;

    public static final String INVALID_DEFAULT_VOICE =
            ConsoleColor.RED_BRIGHT +
            """
            Error: '%voice%' is not a valid voice.
            Please make sure you are only choosing from standard voices.
            Standard voices can be found here:
            https://docs.aws.amazon.com/polly/latest/dg/voicelist.html
            Setting default voice to 'Brian'
            """ + ConsoleColor.RESET;

    public static final String UNKNOWN_COMMAND =
            ConsoleColor.RED_BRIGHT + """
            ERROR: '%command%' is not a recognized command
            """ + ConsoleColor.RESET;

    public static final String NULL_AWS_CREDENTIALS =
            ConsoleColor.RED_BRIGHT + """
            ERROR: AWS credentials are null, please fill them out
            """ + ConsoleColor.RESET;

    public static final String CAUGHT_EXCEPTION =
            ConsoleColor.RED_BRIGHT + """
            ERROR: %error%
            """ + ConsoleColor.RESET;


    public static final String INVALID_INPUT =
            ConsoleColor.RED_BRIGHT + """
            ERROR: Invalid input
            """ + ConsoleColor.RESET;

    public static final String MESSAGE_NOT_PARSABLE =
            ConsoleColor.RED_BRIGHT + """
            ERROR: Message was not parsable, attempted to send: %message%
            """ + ConsoleColor.RESET;

}
