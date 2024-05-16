package simplexity.clitts;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;
import java.util.Scanner;

public class TextToSpeech {

    private static Region AWS_REGION;
    private static final Scanner scanner = new Scanner(System.in);
    private static AmazonPolly polly;
    private static VoiceId VOICE_ID;
    private static boolean runProgram = true;
    private static TextToSpeech instance;
    private static final String boldRed = "\u001b[31;1m";
    private static final String formatReset = "\u001b[0m";
    private static final String boldGreen = "\u001b[32;1m";
    private static final String cyan = "\u001b[36m";
    private static final String yellow = "\u001b[33m";

    public TextToSpeech() {
        polly = new AmazonPollyClient(new BasicAWSCredentials(TTSConfig.getInstance().getAccessID(), TTSConfig.getInstance().getAccessSecret()), new ClientConfiguration());
        polly.setRegion(AWS_REGION);
    }

    public static TextToSpeech getInstance() {
        if (instance == null) {
            instance = new TextToSpeech();
        }
        return instance;
    }

    public InputStream synthesizeSpeech(AmazonPolly polly, String text, VoiceId voice) {
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withText(text)
                .withVoiceId(voice)
                .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
        return synthesizeSpeechResult.getAudioStream();
    }

    public InputStream synthesizeSSMLSpeech(AmazonPolly polly, String text, VoiceId voice) {
        String ssml = "<speak>" + text + "</speak>";
        SynthesizeSpeechRequest synthesizeSpeechRequest;
        try {
            synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                    .withText(ssml)
                    .withTextType(com.amazonaws.services.polly.model.TextType.Ssml)
                    .withVoiceId(voice)
                    .withOutputFormat(OutputFormat.Mp3);
            SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
            return synthesizeSpeechResult.getAudioStream();
        } catch (RuntimeException e) {
            System.out.println(boldRed + "Error: " + e.getMessage() + formatReset);
            return null;
        }
    }

    public String replaceText(String text) {
        for (String key : TTSConfig.getInstance().getReplaceText().keySet()) {
            text = text.replace(key, TTSConfig.getInstance().getReplaceText().get(key));
        }
        for (String key : TTSConfig.getInstance().getVoicePrefixes().keySet()) {
            if (text.startsWith(key)) {
                text = text.replace(key, "");
                VOICE_ID = TTSConfig.getInstance().getVoicePrefixes().get(key);
            }
        }
        return text;
    }

    public static void processSpeech(String text) {
        TextToSpeech tts = getInstance();
        String newText = tts.replaceText(text);
        boolean useSSML = !text.equals(newText);
        try {
            InputStream speechStream;
            if (!useSSML) {
                speechStream = tts.synthesizeSpeech(polly, newText, VOICE_ID);
            } else {
                speechStream = tts.synthesizeSSMLSpeech(polly, newText, VOICE_ID);
            }
            if (speechStream == null) {
                System.out.println(boldRed + "Error: Speech stream is null." + formatReset);
                return;
            }
            AdvancedPlayer player = new AdvancedPlayer(speechStream,
                    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
            player.play();
        } catch (JavaLayerException e) {
            System.out.println(boldRed + "Error playing speech. " + e + formatReset);
        }
    }

    public static void main(String[] args) {
        TTSConfig.getInstance().reloadConfig();
        System.out.println("Type your text then press" + cyan + " Enter" + formatReset + " to convert to speech."
                + "\nType" + cyan + " --reload" + formatReset + " to reload the configuration."
                + "\nType " + cyan + "--exit" + formatReset + " to end the program.");
        VOICE_ID = TTSConfig.getInstance().getDefaultVoice();
        AWS_REGION = TTSConfig.getInstance().getRegion();
        while (runProgram) {
            System.out.println(yellow + "Enter text:" + formatReset);
            String text = scanner.nextLine();
            switch (text) {
                case ("--exit") -> {
                    runProgram = false;
                    System.out.println("Program ended.");
                }
                case ("--help") -> System.out.println("Type your text, press Enter to convert to speech. " +
                        "Type '--exit' to end the program.");
                case ("--reload") -> {
                    TTSConfig.getInstance().reloadConfig();
                    System.out.println(boldGreen + "Config Reloaded" + formatReset);
                }
                default -> {
                    if (TTSConfig.getInstance().getAccessID().isBlank() || TTSConfig.getInstance().getAccessID().isEmpty()) {
                        System.out.println(boldRed + "AWS Access ID is not set. Please set your AWS Access ID and Access secret then use '--reload'" + formatReset);
                        continue;
                    }
                    if (TTSConfig.getInstance().getAccessSecret().isBlank() || TTSConfig.getInstance().getAccessSecret().isEmpty()) {
                        System.out.println(boldRed + "AWS Access Secret is not set. Please set your AWS Access ID and Access secret then use '--reload'" + formatReset);
                        continue;
                    }
                    processSpeech(text);
                }

            }
        }
    }
}