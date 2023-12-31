package simplexity.clitts;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.VoiceId;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;

public class TTSConfig {
    
    private static final HashMap<String, String> replaceText = new HashMap<>();
    private static final HashMap<String, VoiceId> voicePrefixes = new HashMap<>();
    public static Region AWS_REGION;
    public static VoiceId defaultVoice;
    
    
    public static void reloadConfig() {
        Config config = ConfigFactory.load();
        reloadReplaceText(config);
        reloadVoicePrefixes(config);
        reloadRegion(config);
        reloadDefaultVoice(config);
    }
    
    private static void reloadReplaceText(Config config) {
        replaceText.clear();
        config.getConfig("replace-text").entrySet().forEach(entry -> {
            replaceText.put(entry.getKey().replace("\"", ""), entry.getValue().unwrapped().toString());
        });
    }
    
    private static void reloadVoicePrefixes(Config config) {
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
    
    private static void reloadRegion(Config config) {
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
    
    private static void reloadDefaultVoice(Config config) {
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
    
    public static HashMap<String, String> getReplaceText() {
        return replaceText;
    }
    
    
    public static HashMap<String, VoiceId> getVoicePrefixes() {
        return voicePrefixes;
    }
    
}
