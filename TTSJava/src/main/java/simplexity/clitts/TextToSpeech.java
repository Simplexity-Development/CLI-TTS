package simplexity.clitts;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.InputStream;
import java.util.Scanner;

public class TextToSpeech {
    
    private static Region AWS_REGION;
    private static final Scanner scanner = new Scanner(System.in);
    private static AmazonPollyClient polly;
    private static boolean runProgram = true;
    
    public TextToSpeech() {
        polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(), new ClientConfiguration());
        polly.setRegion(AWS_REGION);
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
                    .withTextType(TextType.Ssml)
                    .withVoiceId(voice)
                    .withOutputFormat(OutputFormat.Mp3);
            SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
            return synthesizeSpeechResult.getAudioStream();
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public String replaceText(String text) {
        for (String key : TTSConfig.getReplaceText().keySet()) {
            text = text.replace(key, TTSConfig.getReplaceText().get(key));
            System.out.println("key: " + key);
            System.out.println("value: " + TTSConfig.getReplaceText().get(key));
        }
        return text;
    }
    
    
    public static void main(String[] args) {
        System.out.println("Type your text, press Enter to convert to speech. Type 'exit' to end the program.");
        TTSConfig.reloadConfig();
        VoiceId VOICE_ID = TTSConfig.defaultVoice;
        AWS_REGION = TTSConfig.AWS_REGION;
        TextToSpeech tts = new TextToSpeech();
        InputStream speechStream;
        System.out.println("Using voice: " + VOICE_ID);
        while (runProgram) {
            System.out.println("Enter text:");
            String text = scanner.nextLine();
            switch (text) {
                case ("--exit") -> {
                    runProgram = false;
                    System.out.println("Program ended.");
                }
                case ("--help") ->
                        System.out.println("Type your text, press Enter to convert to speech. Type '--exit' to end the program.");
                default -> {
                    System.out.println(text);
                    String newText = tts.replaceText(text);
                    System.out.println(newText);
                    boolean useSSML = !text.equals(newText);
                    try {
                        if (!useSSML) {
                            speechStream = tts.synthesizeSpeech(polly, newText, VOICE_ID);
                        } else {
                            speechStream = tts.synthesizeSSMLSpeech(polly, newText, VOICE_ID);
                        }
                        if (speechStream == null) {
                            System.out.println("Error: Speech stream is null.");
                            continue;
                        }
                        AdvancedPlayer player = new AdvancedPlayer(speechStream, javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
                        player.setPlayBackListener(new PlaybackListener() {
                            @Override
                            public void playbackStarted(PlaybackEvent event) {
                                System.out.println("Playing speech...");
                            }
                            
                            @Override
                            public void playbackFinished(PlaybackEvent event) {
                                System.out.println("Speech finished playing.");
                            }
                        });
                        player.play();
                    } catch (JavaLayerException e) {
                        System.out.println("Error playing speech. " + e);
                    }
                }
            }
        }
    }
}