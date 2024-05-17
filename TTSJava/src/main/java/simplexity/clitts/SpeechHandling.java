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
import simplexity.clitts.util.Colors;

import java.io.InputStream;

public class SpeechHandling {

    static Region AWS_REGION;
    static AmazonPolly polly;
    static VoiceId VOICE_ID;

    private SpeechHandling() {
        polly = new AmazonPollyClient(new BasicAWSCredentials(TTSConfig.getInstance().getAccessID(), TTSConfig.getInstance().getAccessSecret()), new ClientConfiguration());
        polly.setRegion(AWS_REGION);
    }

    private static SpeechHandling instance;

    public static SpeechHandling getInstance() {
        if (instance == null) instance = new SpeechHandling();
        return instance;
    }

    public void processSpeech(String text) {
        TextToSpeech tts = TextToSpeech.getInstance();
        String newText = replaceText(text);
        boolean useSSML = !text.equals(newText);
        try {
            InputStream speechStream;
            if (!useSSML) {
                speechStream = synthesizeSpeech(polly, newText, VOICE_ID);
            } else {
                speechStream = synthesizeSSMLSpeech(polly, newText, VOICE_ID);
            }
            if (speechStream == null) {
                System.out.println(Colors.boldRed + "Error: Speech stream is null." + Colors.formatReset);
                return;
            }
            AdvancedPlayer player = new AdvancedPlayer(speechStream,
                    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
            player.play();
        } catch (JavaLayerException e) {
            System.out.println(Colors.boldRed + "Error playing speech. " + e + Colors.formatReset);
        }
    }

    public String replaceText(String text) {
        for (String key : TTSConfig.getInstance().getReplaceText().keySet()) {
            text = text.replace(key, TTSConfig.getInstance().getReplaceText().get(key));
        }
        for (String key : TTSConfig.getInstance().getVoicePrefixes().keySet()) {
            if (text.startsWith(key)) {
                text = text.replace(key, "");
                SpeechHandling.VOICE_ID = TTSConfig.getInstance().getVoicePrefixes().get(key);
            }
        }
        return text;
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
            System.out.println(Colors.boldRed + "Error: " + e.getMessage() + Colors.formatReset);
            return null;
        }
    }

    public InputStream synthesizeSpeech(AmazonPolly polly, String text, VoiceId voice) {
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withText(text)
                .withVoiceId(voice)
                .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
        return synthesizeSpeechResult.getAudioStream();
    }
}
