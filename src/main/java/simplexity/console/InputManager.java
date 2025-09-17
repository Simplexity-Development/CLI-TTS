package simplexity.console;

import com.github.twitch4j.TwitchClient;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;
import simplexity.config.ConfigHandler;
import simplexity.config.rules.SpeechEffectRule;
import simplexity.config.rules.VoicePrefixRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputManager {

    private final TwitchClient twitchClient;
    private final String username;

    private LineReader reader;
    private Terminal terminal;
    private Display display;
    private final List<String> messageBuffer = new ArrayList<>();
    private final Map<String, String> idToMessageMap = new HashMap<>();

    public InputManager(TwitchClient twitchClient, String username) {
        this.twitchClient = twitchClient;
        this.username = username;
    }

    public void start() throws IOException {
        terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(new DefaultParser())
                .build();

        display = new Display(terminal, false);
        startInputLoop();
    }

    private void startInputLoop() {
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    String line = reader.readLine("> ");
                    if (line.trim().isEmpty()) continue;
                    if (ConsoleInit.getCommandManager().runCommand(line)) {
                        continue;
                    }

                    if (twitchClient != null && ConfigHandler.getInstance().shouldSendMessages()) {
                        String messageToSend = line;
                        if (ConfigHandler.getInstance().shouldCleanMessages()) {
                            messageToSend = cleanMessage(messageToSend);
                        }
                        twitchClient.getChat().sendMessage(username, messageToSend);
                    }
                    ConfigHandler.getInstance().getSpeechHandler().processSpeech(line);
                } catch (UserInterruptException | EndOfFileException e) {
                    break;
                }
            }
        });
        inputThread.setDaemon(false);
        inputThread.start();
    }

    private String cleanMessage(String input) {
        for (VoicePrefixRule prefixRule : ConfigHandler.getInstance().getVoicePrefixRules()) {
            if (!prefixRule.matches(input)) continue;
            input = prefixRule.applyRule(input);
            break;
        }
        for (SpeechEffectRule effectRule : ConfigHandler.getInstance().getEffectRules()) {
            if (!effectRule.matches(input)) continue;
            input = effectRule.clearMarkdown(input);
        }
        return input;
    }

    public void printMessage(String id, String message){
        idToMessageMap.put(id, message);
        messageBuffer.add(message);
        updateDisplay();
    }

    public void deleteMessage(String id){
        String msg = idToMessageMap.remove(id);
        if (msg != null) {
            messageBuffer.remove(msg);
            updateDisplay();
        }
    }

    public void modifyMessage(String id, String newMessage) {
        String msg = idToMessageMap.get(id);
        if (msg == null) return;
        int index = messageBuffer.indexOf(msg);
        if (index == -1) return;

        messageBuffer.set(index, newMessage);
        idToMessageMap.put(id, newMessage);
        updateDisplay();
    }

    private void updateDisplay(){
        List<AttributedString> attributedLines = messageBuffer.stream()
                .map(AttributedString::fromAnsi)
                .toList();
        display.update(attributedLines, -1);
    }

    public LineReader getReader() {
        return reader;
    }
}
