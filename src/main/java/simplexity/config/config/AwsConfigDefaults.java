package simplexity.config.config;

public class AwsConfigDefaults {
    public static final String DEFAULT_VOICE = "default-voice= \"Brian\"\n";
    public static final String VOICE_PREFIXES = """
                    voice-prefixes {
                      "Sal:"= "Salli"
                      "Kim:"= "Kimberly"
                      "Bri:"= "Brian"
                    }
                    """;
    public static final String AWS_REGION = "aws-region= \"US_EAST_1\"\n";
    public static final String AWS_ACCESS_KEY = "aws-access-id= \"\"\n";
    public static final String AWS_SECRET_KEY = "aws-secret-key= \"\"\n";
}
