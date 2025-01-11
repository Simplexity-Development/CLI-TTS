package simplexity.amazon;

import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.TextType;
import com.amazonaws.services.polly.model.VoiceId;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.Main;
import simplexity.config.TTSConfig;
import simplexity.messages.Errors;
import simplexity.util.Util;

import java.io.InputStream;

public class SpeechHandler {
    private static final Logger logger = LoggerFactory.getLogger(SpeechHandler.class);
    private VoiceId voiceId;

    public SpeechHandler() {
        this.voiceId = TTSConfig.getInstance().getDefaultVoice();
    }

    public void processSpeech(String text) {
        String processedText = replaceText(text);
        boolean useSSML = !text.equals(processedText);
        InputStream speechStream;
        if (useSSML) {
            speechStream = synthesizeSSMLSpeech(processedText, voiceId);
        } else {
            speechStream = synthesizeSpeech(processedText, voiceId);
        }
        if (speechStream == null) {
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", "Speech stream is null"), Level.ERROR);
            return;
        }
        playSpeech(speechStream);
    }

    public String replaceText(String text) {
        for (String key : TTSConfig.getInstance().getReplaceText().keySet()) {
            text = text.replace(key, TTSConfig.getInstance().getReplaceText().get(key));
        }
        for (String key : TTSConfig.getInstance().getVoicePrefixes().keySet()) {
            if (text.startsWith(key)) {
                text = text.replace(key, "");
                voiceId = TTSConfig.getInstance().getVoicePrefixes().get(key);
            }
        }
        return text;
    }

    public InputStream synthesizeSSMLSpeech(String text, VoiceId voice) {
        text = "<speak>" + text + "</speak>";
        SynthesizeSpeechRequest synthesizeSpeechRequest;
        try {
            synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                    .withText(text)
                    .withTextType(TextType.Ssml)
                    .withVoiceId(voice)
                    .withOutputFormat(OutputFormat.Mp3);
            SynthesizeSpeechResult synthesizeSpeechResult = Main.getPollyHandler().getPolly().synthesizeSpeech(synthesizeSpeechRequest);
            return synthesizeSpeechResult.getAudioStream();
        } catch (RuntimeException exception) {
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.ERROR);
            Util.logAndPrint(logger, Errors.MESSAGE_NOT_PARSABLE.replace("%message%", text), Level.ERROR);
            return null;
        }
    }

    public InputStream synthesizeSpeech(String text, VoiceId voice) {
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withText(text)
                .withVoiceId(voice)
                .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthesizeSpeechResult = Main.getPollyHandler().getPolly().synthesizeSpeech(synthesizeSpeechRequest);
        return synthesizeSpeechResult.getAudioStream();
    }

    public void playSpeech(InputStream speechStream) {
        AdvancedPlayer player;
        try {
            player = new AdvancedPlayer(speechStream, FactoryRegistry.systemRegistry().createAudioDevice());
            player.play();
        } catch (Exception exception) {
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.ERROR);
        }
    }
}
