package simplexity.util;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.regex.Pattern;

public class Logging {

    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");

    public static void logAndPrint(Logger logger, String message, Level level) {
        String printMessage = ColorTags.parse(message);
        String logMessage = stripAnsiCodes(printMessage);
        logger.atLevel(level).log(logMessage);
        System.out.println(printMessage);
    }

    public static void log(Logger logger, String message, Level level) {
        logger.atLevel(level).log(message);
    }

    public static String stripAnsiCodes(String text) {
        return ANSI_PATTERN.matcher(text).replaceAll("");
    }
}
