package simplexity.commands;

import org.slf4j.event.Level;
import simplexity.httpserver.LocalServer;
import simplexity.messages.Output;
import simplexity.util.Util;

public class ExitCommand extends Command {

    public ExitCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        Util.logAndPrint(logger, Output.SHUTTING_DOWN, Level.INFO);
        LocalServer.stop();
        System.exit(0);
    }
}
