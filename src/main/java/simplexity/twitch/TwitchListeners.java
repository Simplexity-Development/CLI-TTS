package simplexity.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.DeleteMessageEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.ChatFormat;
import simplexity.config.ConfigHandler;
import simplexity.console.ColorTags;
import simplexity.console.ConsoleInit;
import simplexity.console.Logging;
import simplexity.httpserver.AuthHandler;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TwitchListeners {
    private static final Logger logger = LoggerFactory.getLogger(TwitchListeners.class);

    public static void chatListener() {
        TwitchInit.getTwitchClient().getChat().getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            logger.info("MESSAGE HEARD");
            String user = event.getUser().getName();
            String message = event.getMessage();
            Optional<String> optionalId = event.getMessageEvent().getMessageId();
            if (optionalId.isEmpty()) return;
            String id = optionalId.get();
            String formattedMessage =  getFormat(user, message, event.getPermissions());
            String colorParsed = ColorTags.parse(formattedMessage);
            ConsoleInit.getInputManager().printMessage(id, colorParsed);
            //Logging.logAndPrint(logger, colorParsed, Level.INFO);
        });
        TwitchInit.getTwitchClient().getChat().getEventManager().onEvent(SubscriptionEvent.class, event -> {
            String user = event.getUser().getName();
            Optional<String> messageOptional = event.getMessage();
            if (messageOptional.isEmpty()) return;
            String message = messageOptional.get();
            Optional<String> optionalId = event.getMessageEvent().getMessageId();
            if (optionalId.isEmpty()) return;
            String id = optionalId.get();
            Set<CommandPermission> permissions = event.getMessageEvent().getClientPermissions();
            String formattedMessage = getFormat(user, message, permissions);
            String colorParsed = ColorTags.parse(formattedMessage);
            ConsoleInit.getInputManager().printMessage(id, colorParsed);
        });
        TwitchInit.getTwitchClient().getChat().getEventManager().onEvent(DeleteMessageEvent.class, event -> {
            String user = event.getMessageEvent().getUser().getName();
            Optional<String> optionalId = event.getMessageEvent().getMessageId();
            if (optionalId.isEmpty()) return;
            String id = optionalId.get();
            String formattedDeletedMessage = String.format(ConfigHandler.getInstance().getDeletedMessageFormat().replace("%user%", user));
            formattedDeletedMessage = ColorTags.parse(formattedDeletedMessage);
            ConsoleInit.getInputManager().modifyMessage(id, formattedDeletedMessage);
        });
    }

    private static String getFormat(String user, String message, Set<CommandPermission> permissions) {
        int weight = 0;
        ChatFormat formatToUse = null;
        HashSet<ChatFormat> formats = ConfigHandler.getInstance().getChatFormats();
        for (CommandPermission permission : permissions) {
            for (ChatFormat format : formats) {
                if (!format.getPermission().equals(permission)) continue;
                if (weight > format.getWeight()) continue;
                weight = format.getWeight();
                formatToUse = format;
            }
        }
        if (formatToUse == null) return String.format("%s ➜ %s", user, message);
        return formatToUse.applyFormat(user, message);
    }
}
