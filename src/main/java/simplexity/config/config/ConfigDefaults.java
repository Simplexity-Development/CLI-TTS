package simplexity.config.config;

public class ConfigDefaults {
    public static final String AWS_REGION = "aws-region= \"US_EAST_1\"\n";
    public static final String AWS_ACCESS_KEY = "aws-access-id= \"\"\n";
    public static final String AWS_SECRET_KEY = "aws-secret-key= \"\"\n";
    public static final String SERVER_PORT = "server-port= 3000";
    public static final String REPLACE_TEXT = """
            replace-text {
              " **"= "<prosody volume=\\"x-loud\\" pitch=\\"low\\" rate=\\"slow\\">"
              "** "= "</prosody>"
              "*/"= "</prosody>"
              " ~~"= "<amazon:effect name=\\"whispered\\">"
              "~~ "= "</amazon:effect>"
              "~/"= "</amazon:effect>"
              " __"= "<emphasis level=\\"strong\\">"
              "__ "= "</emphasis>"
              "_/"= "</emphasis>"
              " ++"= "<prosody volume=\\"x-loud\\" rate=\\"x-fast\\" pitch=\\"x-high\\">"
              "++ "= "</prosody>"
              "+/"= "</prosody>"
              " !!"= "<say-as interpret-as=\\"expletive\\">"
              "!! "= "</say-as>"
              "!/"= "</say-as>"
              " - "= "<break time=\\"300ms\\"/>"
              "<3"= "heart emoji"
            }
            """;
    public static final String DEFAULT_VOICE = "default-voice= \"Brian\"\n";
    public static final String VOICE_PREFIXES = """
                    voice-prefixes {
                      "Sal:"= "Salli"
                      "Kim:"= "Kimberly"
                      "Bri:"= "Brian"
                    }
                    """;
}
