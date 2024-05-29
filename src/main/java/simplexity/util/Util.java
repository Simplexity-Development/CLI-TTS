package simplexity.util;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class Util {

    public static void logAndPrint(Logger logger, String message, Level level){
        logger.atLevel(level).log(message);
        System.out.println(message);
    }
}
