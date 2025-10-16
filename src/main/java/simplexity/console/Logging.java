package simplexity.console;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Logging {

    public static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");
    public static final Pattern COLOR_TAGS = Pattern.compile("</?[a-zA-Z-]+>");

    public static void logAndPrint(Logger logger, String message, Level level) {
        String logMessage = stripAnsiCodes(message);
        logMessage = stripColorTags(logMessage);
        logger.atLevel(level).log(logMessage);
        System.out.println(ColorTags.parse(message));
    }

    public static void logAndPrint(Logger logger, String message, ArrayList<String> replaceStrings, Level level) {
        for (String value : replaceStrings) {
            message = message.formatted(value);
        }
        logAndPrint(logger, message, level);
    }



    public static void log(Logger logger, String message, Level level) {
        logger.atLevel(level).log(message);
    }

    public static void onlyPrint(String message){
        System.out.println(ColorTags.parse(message));
    }

    public static String stripAnsiCodes(String text) {
        text = ANSI_PATTERN.matcher(text).replaceAll("");
        return text;
    }

    public static String stripColorTags(String text) {
        text = COLOR_TAGS.matcher(text).replaceAll("");
        return text;
    }
}
