package simplexity.config.locale;

@SuppressWarnings("TextBlockMigration")
public enum Message {


    INVALID_VOICE("error-invalid-voice",
            "<br-red>Error: '%voice%' is not a valid voice. " +
            "\\nPlease make sure you are only choosing from standard voices.</br-red>" +
            "\\nStandard voices can be found here:" +
            "\\n<yellow>https://docs.aws.amazon.com/polly/latest/dg/voicelist.html</yellow>"
    ),
    INVALID_REGION("error-invalid-region",
            "<br-red>Error: '%region%' is not a valid region.</br-red>" +
            "\\n<yellow>Regions can be found here:" +
            "\\nhttps://aws.amazon.com/about-aws/global-infrastructure/regions_az/" +
            "\\nUsing default region of 'US_EAST_1'"
            ),

    UNKNOWN_COMMAND("error-unknown-command","<br-red>ERROR: '%command%' is not a recognized command</br-red>"),

    NULL_AWS_CREDENTIALS("error-aws-credentials-null", "<br-red>ERROR: AWS credentials are null, please fill them out"),

    GENERAL_ERROR("error-default", "<br-red>ERROR: %error%</br-red>"),

    INVALID_INPUT("error-invalid-input", "<br-red>ERROR: Invalid input</br-red>"),

    MESSAGE_NOT_PARSABLE("error-not-parsable", "<br-red>ERROR: Message was not parsable, attempted to send: %message%</br-red>"),
    HELP_HEADER("help-header", "<br-cyan>CLI Text To Speech</br-cyan>"),
    HELP_COMMAND_MESSAGE("help-command-format", "<blue>%command_name%</blue> <white>-</white> <br-blue>%command_description%</br-blue>"),
    RELOAD_MESSAGE("config-reloaded", "<green>Config reloaded</green>"),
    PLEASE_SAVE_AWS_INFO_IN_CONFIG("save-aws-info", "<br-yellow>Please save your AWS credentials in tts-config.conf, then click enter to continue</br-yellow>"),
    SHUTTING_DOWN("shutting-down", "<br-yellow>CLI Text To Speech is closing...</br-yellow>"),

    ;
    private final String path;
    private String message;

    private Message(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    }
