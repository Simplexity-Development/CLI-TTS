package simplexity.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.Main;
import simplexity.config.AbstractConfig;
import simplexity.config.locale.Message;
import simplexity.httpserver.LocalServer;
import simplexity.util.Logging;

import java.util.ArrayList;

public class ReloadCommand extends Command {

    private static final Logger logger = LoggerFactory.getLogger(ReloadCommand.class);
    public ReloadCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        Logging.log(logger, "Reloading configs", Level.INFO);
        reloadConfigs();
        Logging.log(logger, "Stopping local server", Level.INFO);
        LocalServer.stop();
        Logging.log(logger, "Starting local server", Level.INFO);
        LocalServer.run();
        Logging.logAndPrint(logger, Message.RELOAD_MESSAGE.getMessage(), Level.ERROR);
    }

    private void reloadConfigs() {
        ArrayList<AbstractConfig> configs = Main.getConfigs();
        for (AbstractConfig config : configs) {
            config.reloadConfig();
        }
    }
}
