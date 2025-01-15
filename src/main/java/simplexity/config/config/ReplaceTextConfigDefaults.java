package simplexity.config.config;

public class ReplaceTextConfigDefaults  {
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
}
