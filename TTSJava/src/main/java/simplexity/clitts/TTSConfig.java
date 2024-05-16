package simplexity.clitts;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.VoiceId;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class TTSConfig {
    private static TTSConfig instance;
    private final HashMap<String, String> replaceText = new HashMap<>();
    private final HashMap<String, VoiceId> voicePrefixes = new HashMap<>();
    private Region AWS_REGION;
    private VoiceId defaultVoice;
    private String accessID, accessSecret;

    private TTSConfig() {
    }

    public static TTSConfig getInstance() {
        if (instance == null) {
            instance = new TTSConfig();
        }
        return instance;
    }

    public void reloadConfig() {
        Config config = loadConfig();
        reloadReplaceText(config);
        reloadVoicePrefixes(config);
        reloadRegion(config);
        reloadDefaultVoice(config);
        reloadAccessStrings(config);
    }

    private Config loadConfig() {
        String externalConfigPath = "application.conf";
        File externalConfigFile = new File(externalConfigPath);
        if (!externalConfigFile.exists()) {
            createDefaultConfigFile(externalConfigFile);
        }
        return ConfigFactory.parseFile(externalConfigFile).resolve();
    }

    private void createDefaultConfigFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("aws-region = \"US_EAST_1\"\n");
            writer.write("default-voice = \"Kimberly\"\n");
            writer.write("aws-access-id = \"\"\n");
            writer.write("aws-access-secret = \"\"\n");
            writer.write("""
                    replace-text {
                      "**" = "<prosody volume=\\"x-loud\\" pitch=\\"low\\" rate=\\"slow\\">"
                      "/*" = "</prosody>"
                      "*/" = "</prosody>"
                      "~~" = "<amazon:effect name=\\"whispered\\">"
                      "/~" = "</amazon:effect>"
                      "~/" = "</amazon:effect>"
                      "__" = "<emphasis level=\\"strong\\">"
                      "/_" = "</emphasis>"
                      "_/" = "</emphasis>"
                      "++" = "<prosody volume=\\"x-loud\\" rate=\\"x-fast\\" pitch=\\"x-high\\">"
                      "/+" = "</prosody>"
                      "+/" = "</prosody>"
                      "!!" = "<say-as interpret-as=\\"expletive\\">"
                      "/!" = "</say-as>"
                      "!/" = "</say-as>"
                      " - " = "<break time=\\"300ms\\"/>"
                      "<3" = "heart emoji"
                    }
                    """);
            writer.write("""
                    voice-prefixes {
                      "Sal:" = "Salli"
                      "Kim:" = "Kimberly"
                      "Bri:" = "Brian"
                    }
                    """);
        } catch (IOException e) {
            System.out.println("Error creating default config file.");
            e.printStackTrace();
        }
    }

    private void reloadReplaceText(Config config) {
        replaceText.clear();
        config.getConfig("replace-text").entrySet().forEach(entry -> {
            replaceText.put(entry.getKey().replace("\"", ""), entry.getValue().unwrapped().toString());
        });
    }

    private void reloadVoicePrefixes(Config config) {
        voicePrefixes.clear();
        config.getConfig("voice-prefixes").entrySet().forEach(entry -> {
            try {
                VoiceId voiceId = VoiceId.fromValue(String.valueOf(entry.getValue().unwrapped()));
                voicePrefixes.put(entry.getKey().replace("\"", ""), voiceId);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + entry.getValue().unwrapped() +
                        " is not a valid voice. " +
                        "Please make sure you are only choosing from standard voices" +
                        "\nStandard voices can be found here: " +
                        "https://docs.aws.amazon.com/polly/latest/dg/voicelist.html");
            }
        });
    }

    private void reloadRegion(Config config) {
        String region = config.getString("aws-region");
        try {
            AWS_REGION = Region.getRegion(Regions.valueOf(region));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + region +
                    " is not a valid region. " +
                    "\nAWS Regions can be found here: " +
                    "https://aws.amazon.com/about-aws/global-infrastructure/regions_az/");
            AWS_REGION = Region.getRegion(Regions.US_EAST_1); // Default to US East 1 if region is invalid.
            System.out.println("Using default region: " + AWS_REGION.getName());
            System.out.println("Please update your config file to use the correct region.");
        }
    }

    private void reloadDefaultVoice(Config config) {
        String voice = config.getString("default-voice");
        try {
            defaultVoice = VoiceId.valueOf(voice);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + voice + " is not a valid voice. " +
                    "Please make sure you are only choosing from standard voices" +
                    "\nStandard voices can be found here: " +
                    "https://docs.aws.amazon.com/polly/latest/dg/voicelist.html");
            defaultVoice = VoiceId.Kimberly; // Default to Kimberly if voice is invalid.
        }
    }

    private void reloadAccessStrings(Config config) {
        accessID = config.getString("aws-access-id");
        accessSecret = config.getString("aws-access-secret");
    }

    public HashMap<String, String> getReplaceText() {
        return replaceText;
    }


    public HashMap<String, VoiceId> getVoicePrefixes() {
        return voicePrefixes;
    }

    public Region getRegion() {
        return AWS_REGION;
    }

    public VoiceId getDefaultVoice() {
        return defaultVoice;
    }

    public String getAccessID() {
        return accessID;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

}
