package simplexity.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.TTSConfig;
import simplexity.messages.Output;
import simplexity.util.Util;

public class ReloadCommand extends Command {
    private static final Logger logger = LoggerFactory.getLogger(ReloadCommand.class);
    public ReloadCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        TTSConfig.getInstance().reloadConfig();
        Util.logAndPrint(logger, Output.RELOAD_MESSAGE, Level.ERROR);
    }
}
