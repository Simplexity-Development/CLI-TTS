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


}
