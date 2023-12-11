package simplexity.clitts;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.InputStream;
import java.util.Scanner;

public class TextToSpeech {
    
    private static final Region AWS_REGION = Region.getRegion(Regions.US_EAST_1);
    private static final String VOICE_ID = "Joanna"; // Change the voice ID as needed
    private static final Scanner scanner = new Scanner(System.in);
    private static AmazonPollyClient polly;
    private static boolean runProgram = true;
    
    public TextToSpeech() {
        polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(), new ClientConfiguration());
        polly.setRegion(AWS_REGION);
    }
    
    public InputStream synthesizeSpeech(AmazonPolly polly, String text) {
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest().withText(text).withVoiceId(VOICE_ID).withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
        return synthesizeSpeechResult.getAudioStream();
    }
    
    public InputStream synthesizeSSMLSpeech(AmazonPolly polly, String text) {
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest().withText(text).withTextType("ssml").withVoiceId(VOICE_ID).withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
        return synthesizeSpeechResult.getAudioStream();
    }
    
    public String replaceText(String text) {
        for (String key : TTSConfig.getReplaceText().keySet()) {
            text = text.replace(key, TTSConfig.getReplaceText().get(key));
        }
        return text;
    }
    
    
    public static void main(String[] args) {
        System.out.println("Type your text, press Enter to convert to speech. Type 'exit' to end the program.");
        TextToSpeech tts = new TextToSpeech();
        InputStream speechStream;
        TTSConfig.reloadConfig();
        while (runProgram) {
            System.out.println("Enter text:");
            String text = scanner.nextLine();
            String textRef = text;
            switch (text) {
                case ("--exit") -> {
                    runProgram = false;
                    System.out.println("Program ended.");
                }
                case ("--help") ->
                        System.out.println("Type your text, press Enter to convert to speech. Type '--exit' to end the program.");
                case ("--reload") -> {
                    TTSConfig.reloadConfig();
                    System.out.println("Config reloaded.");
                }
                default -> {
                    text = tts.replaceText(text);
                    boolean useSSML = !textRef.equals(text);
                    try {
                        if (!useSSML) {
                            speechStream = tts.synthesizeSpeech(polly, text);
                        } else {
                            speechStream = tts.synthesizeSSMLSpeech(polly, text);
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