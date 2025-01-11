package simplexity.commands;

import org.slf4j.event.Level;
import simplexity.config.TTSConfig;
import simplexity.messages.Output;
import simplexity.util.Util;

public class ReloadCommand extends Command {
    public ReloadCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        TTSConfig.getInstance().reloadConfig();
        Util.logAndPrint(logger, Output.RELOAD_MESSAGE, Level.ERROR);
    }
}
